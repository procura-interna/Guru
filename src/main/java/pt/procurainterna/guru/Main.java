package pt.procurainterna.guru;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
    final Option tokenOption = new Option("apiToken", "apiToken", true, "Path to a file containing the Discord Bot Token");
    final Option roleOption = new Option("role", "role", true, "The name of the role to assign to new members");
    tokenOption.setRequired(true);
    options.addOption(tokenOption);
    options.addOption(roleOption);
    final CommandLineParser parser = new DefaultParser();
    final CommandLine cmd;
    try {
      cmd = parser.parse(options, args);

    } catch (ParseException e) {
      throw new IllegalStateException("Cannot set up parameter parsing", e);
    }

    final String tokenFilePath = cmd.getOptionValue("apiToken");
    final String apiToken = readTokenFromFile(tokenFilePath);
    final String roleToAssing = cmd.getOptionValue("role");

    return new GuruParameters(apiToken, roleToAssing);
  }

  private static String readTokenFromFile(String filePath) {
    try {
      return Files.readString(Path.of(filePath)).trim();
    } catch (IOException e) {
      throw new IllegalStateException("Cannot read API token from file: " + filePath, e);
    }
  }
}
