package pt.procurainterna.guru;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.procurainterna.guru.persistance.JdbcConfig;
import pt.procurainterna.guru.persistance.JdbcConfigReader;

public class StaticArgsLaunch {

  private static final Logger logger = LoggerFactory.getLogger(StaticArgsLaunch.class);

  public static void main(String[] args) {
    logger.info("Bootstrapping Guru Bot...");
    final GuruParameters parameters = parameters(args);

    logger.info("Starting Guru Bot...");
    final Future<Void> guruFuture = new GuruBot().start(parameters);

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      logger.info("Shutting down Guru Bot...");
      guruFuture.cancel(true);
    }));
    logger.info("Added shutdown hook to kill Guru Bot.");

    try {
      logger.info("Waiting for Guru Bot to finish...");
      guruFuture.get();

    } catch (CancellationException e) {
      logger.info("Guru Bot cancelled.");

    } catch (InterruptedException e) {
      logger.info("Guru Bot interrupted", e);

    } catch (ExecutionException e) {
      throw new RuntimeException("Guru Bot execution failed", e);
    }
  }

  private static GuruParameters parameters(String[] args) {
    final Options options = new Options();
    final Option tokenOption = new Option("apiToken", "apiToken", true, "The Discord Bot Token");
    final Option jdbcOption =
        new Option("jdbcConfig", "jdbcConfig", true, "The JDBC Configuration properties file");
    options.addOption(tokenOption);
    options.addOption(jdbcOption);
    logger.info("Options created: {}", options);


    final CommandLineParser parser = new DefaultParser();
    logger.info("Parser created: {}", parser);
    final CommandLine cmd;
    try {
      cmd = parser.parse(options, args);

    } catch (ParseException e) {
      throw new IllegalStateException("Cannot set up parameter parsing", e);
    }
    logger.info("Command line parsed: {}", cmd);


    final String apiTokenPath = cmd.getOptionValue("apiToken");
    final String jdbcConfigValue = cmd.getOptionValue("jdbcConfig");
    logger.info("API Token path: {}", apiTokenPath);
    logger.info("JDBC Config path: {}", jdbcConfigValue);


    logger.info("Reading API Token from path: {}", apiTokenPath);
    final String apiToken = readTokenFromPath(apiTokenPath);
    logger.info("API Token read from path: {}", apiTokenPath);

    logger.info("Reading JDBC Config from path: {}", jdbcConfigValue);
    final JdbcConfig jdbcConfig = JdbcConfigReader.read(Paths.get(jdbcConfigValue));
    logger.info("JDBC Config read from path: {}", jdbcConfigValue);

    logger.info("Arguments parsed");

    return new GuruParameters(apiToken, jdbcConfig);
  }

  public static String readTokenFromPath(String path) {
    try {
      return Files.readString(Path.of(path)).trim();

    } catch (IOException e) {
      throw new IllegalArgumentException("Unable to read token from path.");
    }
  }
}
