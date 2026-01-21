package pt.procurainterna.guru.jda.events;

import java.util.function.Consumer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ShutdownEvent;

@ExtendWith(MockitoExtension.class)
class EntryPointEventListenerTest {

  @Mock
  private Runnable shutdownAction;

  @Mock
  private Consumer<SlashCommandInteractionEvent> commandHandler;

  @InjectMocks
  private EntryPointEventListener listener;

  @Test
  void onEvent_ShutdownEvent_RunsShutdownAction() {
    ShutdownEvent event = org.mockito.Mockito.mock(ShutdownEvent.class);

    listener.onEvent(event);

    Mockito.verify(shutdownAction).run();
    Mockito.verifyNoMoreInteractions(shutdownAction);
    Mockito.verifyNoInteractions(commandHandler);
  }

  @Test
  void onEvent_SlashCommandInteractionEvent_DelegatesToCommandHandler() {
    SlashCommandInteractionEvent event =
        org.mockito.Mockito.mock(SlashCommandInteractionEvent.class);

    listener.onEvent(event);

    Mockito.verify(commandHandler).accept(event);
    Mockito.verifyNoInteractions(shutdownAction);
  }

  @Test
  void onEvent_UnknownEvent_NoSideEffects() {
    GenericEvent event = org.mockito.Mockito.mock(GenericEvent.class);

    listener.onEvent(event);

    Mockito.verifyNoInteractions(shutdownAction);
    Mockito.verifyNoInteractions(commandHandler);
  }
}
