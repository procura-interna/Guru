package pt.procurainterna.guru;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.util.stream.Stream;

public final class PackageResources {

  private PackageResources() {}

  public static InputStream inputStream(String name) {
    return PackageResources.class.getClassLoader().getResourceAsStream(name);
  }

  public static String string(String name, Charset charset) {
    try (final InputStream inputStream = inputStream(name)) {
      return new String(inputStream.readAllBytes(), charset);

    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  public static Reader reader(String name, Charset charset) {
    final InputStream inputStream = inputStream(name);

    return method(inputStream, charset);
  }

  public static Stream<String> lines(String name, Charset charset) {
    final BufferedReader reader = method(inputStream(name), charset);

    return reader.lines();
  }

  private static BufferedReader method(final InputStream inputStream, Charset charset) {
    return new BufferedReader(new InputStreamReader(inputStream, charset));
  }

}
