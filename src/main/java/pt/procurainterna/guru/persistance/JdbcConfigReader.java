package pt.procurainterna.guru.persistance;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Properties;

import pt.procurainterna.guru.PropertiesFiles;

public class JdbcConfigReader {

  public static JdbcConfig read(final Path jdbcConfigPath) {
    final Properties properties = PropertiesFiles.read(jdbcConfigPath, StandardCharsets.UTF_8);

    final String url = properties.getProperty("url", "");
    final String user = properties.getProperty("user", "");
    final String password = properties.getProperty("password", "");
    final String driverClassName = properties.getProperty("driverClassName", "");

    return new JdbcConfig(driverClassName, password, url, user);
  }

}
