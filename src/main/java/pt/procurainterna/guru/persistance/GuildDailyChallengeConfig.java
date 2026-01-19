package pt.procurainterna.guru.persistance;

public class GuildDailyChallengeConfig {

  public Long id;
  public String guildId;
  public String channelId;

  public GuildDailyChallengeConfig() {
  }

  public GuildDailyChallengeConfig(Long id, String guildId, String channelId) {
    this.id = id;
    this.guildId = guildId;
    this.channelId = channelId;
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
}
