package pt.procurainterna.guru.cdi;

import com.google.inject.Inject;
import com.google.inject.Injector;

public class GuiceCDIProvider implements CDIProvider {

  private final Injector injector;

  @Inject
  public GuiceCDIProvider(Injector injector) {
    this.injector = injector;
  }

  @Override
  public <T> T getInstance(Class<T> clazz) {
    return injector.getInstance(clazz);
  }

}
