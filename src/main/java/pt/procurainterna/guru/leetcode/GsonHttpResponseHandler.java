package pt.procurainterna.guru.leetcode;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;

public class GsonHttpResponseHandler<T> implements HttpClientResponseHandler<T> {

  private final Gson gson;
  private final Class<T> clazz;

  public GsonHttpResponseHandler(Gson gson, Class<T> clazz) {
    this.gson = gson;
    this.clazz = clazz;
  }

  @Override
  public T handleResponse(ClassicHttpResponse response) throws IOException {
    return gson.fromJson(
        new InputStreamReader(response.getEntity().getContent(), StandardCharsets.UTF_8), clazz);
  }
}
