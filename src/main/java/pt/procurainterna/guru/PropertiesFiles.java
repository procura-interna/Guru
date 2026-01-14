package pt.procurainterna.guru;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class PropertiesFiles {

  private PropertiesFiles() {}

  public static Properties read(Path path, Charset charset) {
    try (final var reader = Files.newBufferedReader(path, charset)) {
      return read(reader);

    } catch (IOException e) {
      throw new IllegalArgumentException("Unable to read properties file", e);
    }
  }

  public static Properties read(InputStream inputStream, Charset charset) {
    try (final var reader = new BufferedReader(new InputStreamReader(inputStream, charset))) {
      return read(reader);

    } catch (IOException e) {
      throw new IllegalArgumentException("Unable to read properties file", e);
    }
  }

  public static Properties read(Reader reader) {
    final Properties properties = new Properties();
    try {
      properties.load(reader);

    } catch (IOException e) {
      throw new IllegalArgumentException("Unable to read properties file", e);
    }

    return properties;
  }

}
