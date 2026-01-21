package pt.procurainterna.guru.jda.events;

import java.util.function.Consumer;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ShutdownEvent;
import net.dv8tion.jda.api.hooks.EventListener;

public class EntryPointEventListener implements EventListener {

  private final Runnable shutdownAction;
  private final Consumer<SlashCommandInteractionEvent> commandHandler;

  public EntryPointEventListener(Runnable shutdownAction,
      Consumer<SlashCommandInteractionEvent> commandHandler) {
    this.shutdownAction = shutdownAction;
    this.commandHandler = commandHandler;
  }

  @Override
  public void onEvent(GenericEvent event) {
    if (event instanceof ShutdownEvent) {
      shutdownAction.run();

    } else if (event instanceof SlashCommandInteractionEvent slashCommandInteractionEvent) {
      commandHandler.accept(slashCommandInteractionEvent);

    } else {
      // do nothing
    }
  }



}
