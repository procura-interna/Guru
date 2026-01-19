package pt.procurainterna.guru;

import jakarta.inject.Inject;
import net.dv8tion.jda.api.entities.channel.unions.GuildChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import pt.procurainterna.guru.persistance.DailyChallengeConfigRepository;

public class SetDlcChannelCommand {

  private final DailyChallengeConfigRepository repository;

  @Inject
  public SetDlcChannelCommand(DailyChallengeConfigRepository repository) {
    this.repository = repository;
  }

  public void execute(SlashCommandInteractionEvent event) {
    final OptionMapping option = event.getOption("channel");
    if (option == null) {
      event.getHook().sendMessage("Please specify a channel to set.").queue();
      return;
    }

    final GuildChannelUnion channel = option.getAsChannel();
    if (channel == null) {
      event.getHook().sendMessage("Please specify a channel to set.").queue();
      return;
    }

    try {
      final String guildId = event.getGuild().getId();
      String channelId = channel.getId();
      setConfig(guildId, channelId);

    } catch (final RuntimeException e) {
      event.getHook().sendMessage("Failed to set channel.").queue();
      throw new RuntimeException(e);
    }

    event.getHook().sendMessage("Channel set").setEphemeral(true).queue();
  }

  private void setConfig(String guildId, String channelId) {
    repository.removeIfExists(guildId);
    repository.ensureExists(guildId, channelId);
  }

}
