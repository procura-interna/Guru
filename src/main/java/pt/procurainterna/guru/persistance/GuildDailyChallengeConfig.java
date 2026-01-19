package pt.procurainterna.guru.persistance;

import java.time.Instant;

public class GuildDailyChallengeConfig {

  public Long id;
  public String guildId;
  public String channelId;
  public Instant dbTimestamp;

  public GuildDailyChallengeConfig() {
  }

  public GuildDailyChallengeConfig(Long id, String guildId, String channelId, Instant dbTimestamp) {
    this.id = id;
    this.guildId = guildId;
    this.channelId = channelId;
    this.dbTimestamp = dbTimestamp;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getGuildId() {
    return guildId;
  }

  public void setGuildId(String guildId) {
    this.guildId = guildId;
  }

  public String getChannelId() {
    return channelId;
  }

  public void setChannelId(String channelId) {
    this.channelId = channelId;
  }

  public Instant getDbTimestamp() {
    return dbTimestamp;
  }

  public void setDbTimestamp(Instant dbTimestamp) {
    this.dbTimestamp = dbTimestamp;
  }
}
