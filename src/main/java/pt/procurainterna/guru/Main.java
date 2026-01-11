package pt.procurainterna.guru;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.concurrent.Future;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

  private static final Logger logger = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) {
    logger.info("Starting Guru Bot...");
    final GuruParameters parameters = parameters(args);

    final Future<Void> guruFuture = new GuruBot().start(parameters);

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      logger.info("Shutting down Guru Bot...");
      guruFuture.cancel(true);
    }));

    try {
      guruFuture.get();
    } catch (Exception e) {
      logger.error("Guru Bot runtime failed", e);
    }
  }

  private static GuruParameters parameters(String[] args) {
    final Options options = new Options();
    final Option tokenOption = new Option("apiToken", "apiToken", true, "The Discord Bot Token");
    final Option jdbcOption =
        new Option("jdbcConfig", "jdbcConfig", true, "The JDBC Configuration properties file");
    options.addOption(tokenOption);
    options.addOption(jdbcOption);
    final CommandLineParser parser = new DefaultParser();
    final CommandLine cmd;
    try {
      cmd = parser.parse(options, args);

    } catch (ParseException e) {
      throw new IllegalStateException("Cannot set up parameter parsing", e);
    }

    final String apiTokenPath = cmd.getOptionValue("apiToken");
    final String apiToken = readTokenFromPath(apiTokenPath);
    final String jdbcConfigValue = cmd.getOptionValue("jdbcConfig");

    final Properties properties = new Properties();
    try (final var inputStream = Files.newInputStream(Paths.get(jdbcConfigValue))) {
      properties.load(inputStream);

    } catch (IOException e) {
      throw new IllegalStateException("Cannot load JDBC configuration", e);
    }

    final String url = properties.getProperty("url", "");
    final String user = properties.getProperty("user", "");
    final String password = properties.getProperty("password", "");
    final String driverClassName = properties.getProperty("driverClassName", "");

    return new GuruParameters(apiToken, new JdbcConfig(driverClassName, password, url, user));
  }
  public static String readTokenFromPath (String path) {
    try {
      return Files.readString(Path.of(path)).trim();
    } catch (IOException e) {
      throw new IllegalArgumentException("Unable to read token from path.");
    }
  }
}
