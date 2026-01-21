package pt.procurainterna.guru.persistance.hikari;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import pt.procurainterna.guru.persistance.JdbcConfig;

public class ConfigToHikariDataSource {

  public HikariDataSource dataSource(final JdbcConfig jdbcConfig) {
    final HikariConfig config = new HikariConfig();
    config.setJdbcUrl(jdbcConfig.url);
    config.setUsername(jdbcConfig.user);
    config.setPassword(jdbcConfig.password);
    config.setDriverClassName(jdbcConfig.driverClassName);

    return new HikariDataSource(config);
  }


}
