package pt.procurainterna.guru.persistance;

import java.util.Optional;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.Query;
import org.jdbi.v3.core.statement.Update;

import jakarta.inject.Inject;
import pt.procurainterna.guru.model.GuildInitialRole;

public class JdbiGuildInitialRoleRepository implements GuildInitialRoleRepository {

  private final Jdbi jdbi;

  @Inject
  public JdbiGuildInitialRoleRepository(Jdbi jdbi) {
    this.jdbi = jdbi;
  }

  @Override
  public void ensureExists(String guildId, String roleId) {
    jdbi.useHandle(handle -> {
      final Query query = handle.createQuery("SELECT 1 FROM guild_starting_role WHERE guild_id = ?")
          .bind(0, guildId);
      final boolean exists = query.mapTo(Integer.class).findFirst().isPresent();

      final Update update;
      if (exists) {
        update =
            handle.createUpdate("UPDATE guild_starting_role SET role_id = ? WHERE guild_id = ?");

      } else {
        update = handle
            .createUpdate("INSERT INTO guild_starting_role (role_id, guild_id) VALUES (?, ?)");
      }
      update.bind(0, roleId).bind(1, guildId).execute();

      update.execute();
    });
  }

  @Override
  public boolean deleteForGuild(String guildId) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public Optional<GuildInitialRole> forGuild(String guildId) {
    return jdbi.withHandle(handle -> {
      final Query query = handle.createQuery("SELECT * FROM guild_starting_role WHERE guild_id = ?")
          .bind(0, guildId);

      return query.mapTo(GuildInitialRole.class).findFirst();
    });
  }

}
