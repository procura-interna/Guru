package pt.procurainterna.guru.cdi;

public interface CDIProvider {

  <T> T getInstance(Class<T> clazz);

}
