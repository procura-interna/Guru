package pt.procurainterna.guru.persistance;

import jakarta.inject.Inject;
import java.nio.charset.StandardCharsets;

import org.jdbi.v3.core.Jdbi;

import pt.procurainterna.guru.PackageResources;

public class DefaultDbInitializer implements DbInitializer {

  private final Jdbi jdbi;

  @Inject
  public DefaultDbInitializer(Jdbi jdbi) {
    this.jdbi = jdbi;
  }

  @Override
  public void initialize() {
    jdbi.useHandle(handle -> PackageResources.lines("db_init.sql", StandardCharsets.UTF_8)
        .forEach(line -> {
          try {
            handle.execute(line);
          } catch (RuntimeException e) {
            e.printStackTrace();
          }
        }));
  }



}
