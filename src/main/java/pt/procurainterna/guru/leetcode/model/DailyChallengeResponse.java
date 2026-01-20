package pt.procurainterna.guru.leetcode.model;

public class DailyChallengeResponse {

  public DailyChallengeResponseData data;

  public DailyChallengeResponse() {}

  public DailyChallengeResponse(DailyChallengeResponseData data) {
    this.data = data;
  }

  public DailyChallengeResponseData getData() {
    return data;
  }

  public void setData(DailyChallengeResponseData data) {
    this.data = data;
  }

}
