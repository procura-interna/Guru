package pt.procurainterna.guru.persistance;

import jakarta.inject.Inject;
import java.time.Instant;
import java.util.Optional;
import org.jdbi.v3.core.Jdbi;

public class JdbiDailyChallengeConfigRepository implements DailyChallengeConfigRepository {

  private final Jdbi jdbi;

  @Inject
  public JdbiDailyChallengeConfigRepository(Jdbi jdbi) {
    this.jdbi = jdbi;
  }

  @Override
  public void ensureExists(String guildId, String channelId) {
    jdbi.useHandle(handle -> {
      final Long id = handle.select(
          "SELECT c.dbId FROM daily_lc_challenge_guild_channel c WHERE c.guildId = ? ORDER BY c.dbTimestamp DESC",
          channelId).mapTo(Long.class).findFirst().orElse(null);

      final Instant now = Instant.now();

      if (id == null) {
        handle.createUpdate(
                "INSERT INTO daily_lc_challenge_guild_channel(guildId, channelId, dbTimestamp) VALUES(?, ?, ?)")
            .bind(0, guildId).bind(1, channelId).bind(2, now).execute();

      } else {
        handle.createUpdate(
                "UPDATE daily_lc_challenge_guild_channel c SET c.channelId = ?, c.dbTimestamp = ? WHERE c.dbId = ?")
            .bind(0, guildId).bind(1, now).bind(2, id).execute();
      }
    });
  }

  @Override
  public Optional<GuildDailyChallengeConfig> forGuild(String guildId) {
    return jdbi.withHandle(handle -> handle.createQuery(
            "SELECT c.dbId, c.guildId, c.channelId, c.dbTimestamp FROM daily_lc_challenge_guild_channel c WHERE c.guildId = ? ORDER BY c.dbTimestamp DESC")
        .bind(0, guildId)
        .setFetchSize(1).setMaxRows(1).mapToBean(GuildDailyChallengeConfig.class).findFirst());
  }

  @Override
  public void removeIfExists(String guildId) {
    jdbi.withHandle(handle -> handle.createUpdate(
            "DELETE FROM daily_lc_challenge_guild_channel WHERE guildId = ?").bind(0, guildId)
        .execute());
  }
}
