package pt.procurainterna.guru;

import pt.procurainterna.guru.persistance.JdbcConfig;

public class GuruParameters {
  public final transient String apiToken;
  public final JdbcConfig jdbcConfig;

  public GuruParameters(String apiToken, JdbcConfig jdbcConfig) {
    this.apiToken = apiToken;
    this.jdbcConfig = jdbcConfig;
  }

}
