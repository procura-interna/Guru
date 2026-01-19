package pt.procurainterna.guru.leetcode;

public class ActiveDailyCodingChallengeQuestion {

  public String title;
  public String difficulty;
  public String frontendQuestionId;

  public ActiveDailyCodingChallengeQuestion() {
  }

  public ActiveDailyCodingChallengeQuestion(String title, String difficulty,
      String frontendQuestionId) {
    this.title = title;
    this.difficulty = difficulty;
    this.frontendQuestionId = frontendQuestionId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDifficulty() {
    return difficulty;
  }

  public void setDifficulty(String difficulty) {
    this.difficulty = difficulty;
  }

  public String getFrontendQuestionId() {
    return frontendQuestionId;
  }

  public void setFrontendQuestionId(String frontendQuestionId) {
    this.frontendQuestionId = frontendQuestionId;
  }
}
