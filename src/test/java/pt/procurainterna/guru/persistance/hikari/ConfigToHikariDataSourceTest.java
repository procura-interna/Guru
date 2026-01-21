package pt.procurainterna.guru.persistance.hikari;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import com.zaxxer.hikari.HikariDataSource;

import pt.procurainterna.guru.persistance.JdbcConfig;

class ConfigToHikariDataSourceTest {

  @Test
  void dataSource_ValidConfig_ReturnsConfiguredDataSource() {
    JdbcConfig config = new JdbcConfig("org.sqlite.JDBC", "password", "jdbc:sqlite::memory:", "sa");
    ConfigToHikariDataSource factory = new ConfigToHikariDataSource();

    try (HikariDataSource hikariDS = factory.dataSource(config)) {
      assertEquals("jdbc:sqlite::memory:", hikariDS.getJdbcUrl());
      assertEquals("sa", hikariDS.getUsername());
      assertEquals("password", hikariDS.getPassword());
      assertEquals("org.sqlite.JDBC", hikariDS.getDriverClassName());
    }
  }
}
