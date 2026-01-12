package pt.procurainterna.guru.cdi;

import org.jdbi.v3.core.Jdbi;

import com.google.inject.AbstractModule;

import pt.procurainterna.guru.persistance.GuildInitialRoleRepository;
import pt.procurainterna.guru.persistance.JdbiGuildInitialRoleRepository;

public class GuruCdiModule extends AbstractModule {

  private final Jdbi jdbi;

  public GuruCdiModule(Jdbi jdbi) {
    this.jdbi = jdbi;
  }

  @Override
  protected void configure() {
    bind(Jdbi.class).toInstance(jdbi);
    bind(GuildInitialRoleRepository.class).to(JdbiGuildInitialRoleRepository.class);
  }

}
