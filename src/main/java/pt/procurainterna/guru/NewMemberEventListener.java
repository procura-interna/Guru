package pt.procurainterna.guru;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import pt.procurainterna.guru.model.GuildInitialRole;
import pt.procurainterna.guru.persistance.GuildInitialRoleRepository;

public class NewMemberEventListener extends ListenerAdapter {

  private static final Logger logger = LoggerFactory.getLogger(NewMemberEventListener.class);

  private final GuildInitialRoleRepository repository;

  public NewMemberEventListener(GuildInitialRoleRepository repository) {
    this.repository = repository;
  }

  @Override
  public void onGuildMemberJoin(GuildMemberJoinEvent event) {
    logger.info("New member joined: {} | {}", event.getMember().getUser().getId(),
        event.getMember().getUser().getName());

    final Member member = event.getMember();
    final Guild guild = event.getGuild();

    final Optional<String> roleId = roleId(guild);
    if (roleId.isEmpty()) {
      logger.info("No role configured for guild {}", guild.getId());
      return;
    }

    final Role role = role(guild, roleId.get());

    logger.info("Assigning role {} to member {}", role.getId(), member.getUser().getId());
    guild.addRoleToMember(member, role)
        .onSuccess(ignore -> logger.info("Role assigned successfully")).queue();
  }

  private Role role(final Guild guild, final String roleId) throws IllegalStateException {
    logger.info("Looking for role: {}", roleId);

    final Role role = guild.getRoleById(roleId);
    if (role == null) {
      throw new IllegalStateException("Role " + roleId + " not found");
    }

    logger.info("Found role: {}", role.getId());

    return role;
  }

  private Optional<String> roleId(final Guild guild) {
    return repository.forGuild(guild.getId()).map(GuildInitialRole::roleId);
  }

}
