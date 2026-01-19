package pt.procurainterna.guru.leetcode;

import com.google.gson.Gson;
import jakarta.inject.Inject;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.core5.http.io.entity.StringEntity;

public class HttpClientDailyChallengeFetcher implements DailyChallengeFetcher {

  private final HttpClient httpClient;
  private final Gson gson;

  @Inject
  public HttpClientDailyChallengeFetcher(HttpClient httpClient, Gson gson) {
    this.httpClient = httpClient;
    this.gson = gson;
  }

  @Override
  public DailyChallengeResponse fetch() {
    final HttpGet httpGet = new HttpGet("https://leetcode.com/graphql");

    httpGet.setEntity(new StringEntity(
        "{\"query\":\"query questionOfToday { activeDailyCodingChallengeQuestion { date link question { title difficulty frontendQuestionId: questionFrontendId } } }\"}",
        StandardCharsets.UTF_8));

    httpGet.setHeader("Accept", "application/json");

    try {
      return httpClient.execute(httpGet, new GsonHttpResponseHandler<>(gson, DailyChallengeResponse.class));

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
