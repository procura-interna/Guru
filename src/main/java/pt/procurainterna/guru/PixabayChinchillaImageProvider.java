package pt.procurainterna.guru;

import static java.time.temporal.ChronoUnit.SECONDS;

import com.google.gson.GsonBuilder;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Random;

public class PixabayChinchillaImageProvider implements ChinchillaImageProvider {

  private static final String pixabayKey = System.getenv("PIXABAY_KEY");

  @Override
  public InputStream getImage() {
    try (final HttpClient client = HttpClient.newBuilder().build()) {
      final URI imageURI = getRandomChinchillaURI();

      var request = HttpRequest.newBuilder()
          .uri(imageURI)
          .timeout(Duration.of(10, SECONDS))
          .GET()
          .build();

      var response = client.send(request, BodyHandlers.ofByteArray());

      return new ByteArrayInputStream(response.body());

    } catch (URISyntaxException | IOException e) {
      throw new RuntimeException(e);

    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException(e);
    }
  }

  private static URI getRandomChinchillaURI() throws URISyntaxException {
    var chinchillaMetaDataTotal = getChinchillaImageMetaData(1, 3);
    var total = chinchillaMetaDataTotal.total;

    var randomIndex = new Random().nextInt(total);
    return getNthChinchillaURI(randomIndex);
  }

  private static ChinchillaMetaData getChinchillaImageMetaData(int page, int perPage)
      throws URISyntaxException {
    try {
      var uri = new URI(createUriString(page, perPage));
      var chinchillaMetadataString = makeRequest(uri);

      return new GsonBuilder().create()
          .fromJson(chinchillaMetadataString, ChinchillaMetaData.class);


    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }

  private static URI getNthChinchillaURI(int n) throws URISyntaxException {
    int page = (n / 3) + 1;
    int idx = n % 3;

    var chinchillaImageMeta = getChinchillaImageMetaData(page, 3);

    if (chinchillaImageMeta.hits.isEmpty()) {
      throw new RuntimeException("Pixabay returned no pictures");
    }

    return new URI(chinchillaImageMeta.hits.get(idx).webformatURL);
  }

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

  private static String createUriString(int page, int perPage) {
    return "https://pixabay.com/api/?key=" + URLEncoder.encode(pixabayKey, StandardCharsets.UTF_8)
        + "&q=chinchilla&image_type=photo&page=" + page + "&per_page=" + perPage;
  }

  private static class ChinchillaMetaData {

    public int total;
    public int totalHits;
    public List<Hit> hits;

    private static class Hit {

      public String webformatURL;
    }
  }
}