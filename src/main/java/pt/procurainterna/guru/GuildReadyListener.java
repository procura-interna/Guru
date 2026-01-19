package pt.procurainterna.guru;

import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class GuildReadyListener implements Consumer<GuildReadyEvent> {

  private static final Logger logger = LoggerFactory.getLogger(GuildReadyListener.class);

  public void accept(GuildReadyEvent event) {
    logger.info("Guild ready: {}", event.getGuild().getName());

    event.getGuild().upsertCommand("setrole", "Set the role to assign to new users.")
        .addOption(OptionType.ROLE, "role", "Role to be assigned.").queue();

    event.getGuild().upsertCommand("setdlcchannel", "Set the channel where LeetCode daily challenges are sent to.")
        .addOption(OptionType.CHANNEL, "channel", "Channel to set.").queue();
  }

}
