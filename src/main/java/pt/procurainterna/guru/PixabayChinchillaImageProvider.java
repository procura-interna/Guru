package pt.procurainterna.guru;

import static java.time.temporal.ChronoUnit.SECONDS;

import com.google.gson.GsonBuilder;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.List;

public class PixabayChinchillaImageProvider implements ChinchillaImageProvider {

  private static class ChinchillaMetaData{
    public int total;
    public int totalHits;
    public List<Hit> hits;

    private static class Hit{
      public String webformatURL;
    }
  }

  static String pixabayKey = System.getenv("PIXABAY_KEY");

  private static String makeRequest(URI uri) {
    try {
      HttpResponse<String> response;
      try (HttpClient client = HttpClient.newHttpClient()) {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(uri)
            .build();

        response = client.send(request, BodyHandlers.ofString());
      }

      return (response.body());

    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  private static ChinchillaMetaData getChinchillaImageMetaData() {
    try {

      // DANGER: use URI
      // TODO: use URIDecoder rather than concatenating the string manually
      var uriString =
          "https://pixabay.com/api/?key=" + pixabayKey + "&q=chinchilla&image_type=photo";

      var uri = new URI(uriString);

      var chinchillaMetadataString = makeRequest(uri);

      return new GsonBuilder().create()
          .fromJson(chinchillaMetadataString, ChinchillaMetaData.class);
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }

  private static URI getChinchillaURI() throws URISyntaxException {
    var chinchillaImageMeta = getChinchillaImageMetaData();

    // TODO: handle if there are no hits
    // TODO: hanlde if chinchillaImageMeta is null
    if(chinchillaImageMeta.hits.size() <= 0){
      throw new RuntimeException();
    }

    int randomIdx= (int)(Math.random() * (chinchillaImageMeta.hits.size()-1));


    return new URI(chinchillaImageMeta.hits.get(randomIdx).webformatURL);
  }

  @Override
  public InputStream getImage() {
    try {
      URI imageURI = getChinchillaURI();
      HttpClient client;
      client = HttpClient.newBuilder().build();

      var request = HttpRequest.newBuilder()
          .uri(imageURI)
          .timeout(Duration.of(10, SECONDS))
          .GET()
          .build();

      var response = client.send(request, BodyHandlers.ofByteArray());

      client.close();
      return new ByteArrayInputStream(response.body());

    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
