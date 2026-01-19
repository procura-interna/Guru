package pt.procurainterna.guru.persistance;

import java.util.Optional;

public interface DailyChallengeConfigRepository {

  void ensureExists(String guildId, String channelId);

  Optional<GuildDailyChallengeConfig> forGuild(String guildId);

  void removeIfExists(String guildId);

}
