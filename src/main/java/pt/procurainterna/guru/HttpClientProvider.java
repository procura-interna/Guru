package pt.procurainterna.guru;

import jakarta.inject.Provider;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;

public class HttpClientProvider implements Provider<HttpClient> {

  @Override
  public HttpClient get() {
    return HttpClientBuilder.create().build();
  }
}
