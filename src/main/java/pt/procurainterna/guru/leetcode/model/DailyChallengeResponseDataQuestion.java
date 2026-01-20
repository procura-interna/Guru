package pt.procurainterna.guru.leetcode.model;

import java.time.LocalDate;

public class DailyChallengeResponseDataQuestion {

  public LocalDate date;
  public String link;
  private ActiveDailyCodingChallengeQuestion question;

  public DailyChallengeResponseDataQuestion() {}

  public DailyChallengeResponseDataQuestion(LocalDate date, String link,
      ActiveDailyCodingChallengeQuestion question) {
    this.date = date;
    this.link = link;
    this.question = question;
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public String getLink() {
    return link;
  }

  public void setLink(String link) {
    this.link = link;
  }

  public ActiveDailyCodingChallengeQuestion getQuestion() {
    return question;
  }

  public void setQuestion(ActiveDailyCodingChallengeQuestion question) {
    this.question = question;
  }
}
