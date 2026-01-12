package pt.procurainterna.guru;

import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.api.events.session.ShutdownEvent;

public class ShutdownListener implements Consumer<ShutdownEvent> {

  private static final Logger logger = LoggerFactory.getLogger(ShutdownListener.class);

  private final Runnable onShutdown;

  public ShutdownListener(Runnable onShutdown) {
    this.onShutdown = onShutdown;
  }

  @Override
  public void accept(ShutdownEvent event) {
    logger.info("Shutdown event received: {}", event.getClass().getSimpleName());
    onShutdown.run();
  }

}
