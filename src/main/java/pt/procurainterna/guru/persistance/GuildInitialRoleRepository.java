package pt.procurainterna.guru.persistance;

import java.util.Optional;

import pt.procurainterna.guru.model.GuildInitialRole;

public interface GuildInitialRoleRepository {

  void ensureExists(String guildId, String roleId);

  boolean deleteForGuild(String guildId);

  Optional<GuildInitialRole> forGuild(String guildId);

}
