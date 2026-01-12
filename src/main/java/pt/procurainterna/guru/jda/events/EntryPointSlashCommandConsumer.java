package pt.procurainterna.guru.jda.events;

import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Injector;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import pt.procurainterna.guru.SetRoleCommand;

public class EntryPointSlashCommandConsumer implements Consumer<SlashCommandInteractionEvent> {

  private static final Logger logger =
      LoggerFactory.getLogger(EntryPointSlashCommandConsumer.class);

  private final Injector injector;

  public EntryPointSlashCommandConsumer(Injector injector) {
    this.injector = injector;
  }

  @Override
  public void accept(SlashCommandInteractionEvent event) {
    event.deferReply().queue();

    final String commandName = event.getName();

    switch (commandName) {
      case "setrole":
        final SetRoleCommand setRoleCommand = injector.getInstance(SetRoleCommand.class);
        setRoleCommand.execute(event);
        break;

      // TODO: removerole

      default:
        event.getHook().sendMessage("Unknown command").setEphemeral(true).queue();
        logger.error("Unknown command: {}", commandName);
        break;
    }
  }

}
