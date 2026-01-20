package pt.procurainterna.guru.leetcode.model;

public class DailyChallengeResponseData {

  public DailyChallengeResponseDataQuestion activeDailyCodingChallengeQuestion;

  public DailyChallengeResponseData() {}

  public DailyChallengeResponseData(
      DailyChallengeResponseDataQuestion activeDailyCodingChallengeQuestion) {
    this.activeDailyCodingChallengeQuestion = activeDailyCodingChallengeQuestion;
  }

  public DailyChallengeResponseDataQuestion getActiveDailyCodingChallengeQuestion() {
    return activeDailyCodingChallengeQuestion;
  }

  public void setActiveDailyCodingChallengeQuestion(
      DailyChallengeResponseDataQuestion activeDailyCodingChallengeQuestion) {
    this.activeDailyCodingChallengeQuestion = activeDailyCodingChallengeQuestion;
  }
}
