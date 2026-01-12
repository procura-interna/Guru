package pt.procurainterna.guru;

import com.google.inject.Inject;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import pt.procurainterna.guru.persistance.GuildInitialRoleRepository;

public class SetRoleCommand {

  private final GuildInitialRoleRepository repository;

  @Inject
  public SetRoleCommand(GuildInitialRoleRepository repository) {
    this.repository = repository;
  }

  public void execute(SlashCommandInteractionEvent event) {
    event.deferReply(false).queue();

    final OptionMapping option = event.getOption("role");
    if (option == null) {
      event.getHook().sendMessage("Please provide a role").setEphemeral(true).queue();
      return;
    }

    final Role role = option.getAsRole();
    if (role == null) {
      event.getHook().sendMessage("Please provide a valid role").setEphemeral(true).queue();
      return;
    }

    try {
      setConfig(event.getGuild().getId(), role.getId());

    } catch (Exception e) {
      event.getHook().sendMessage("Failed to set role").setEphemeral(true).queue();
      throw new RuntimeException(e);
    }

    event.getHook().sendMessage("Role set").setEphemeral(true).queue();
  }

  private void setConfig(final String guildId, final String roleId) {
    repository.ensureExists(guildId, roleId);
  }

}
