package pt.procurainterna.guru;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

class PackageResourcesTest {

  private static final String TEST_FILE = "test.txt";
  private static final String EXPECTED_CONTENT = "Hello World";

  @Test
  void inputStream_ValidResource_ReturnsInputStream() throws IOException {
    try (InputStream inputStream = PackageResources.inputStream(TEST_FILE)) {
      assertNotNull(inputStream, "InputStream should not be null");
      String content = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
      assertEquals(EXPECTED_CONTENT, content);
    }
  }

  @Test
  void string_ValidResource_ReturnsContent() {
    String content = PackageResources.string(TEST_FILE, StandardCharsets.UTF_8);
    assertEquals(EXPECTED_CONTENT, content);
  }

  @Test
  void reader_ValidResource_ReturnsReader() throws IOException {
    try (Reader reader = PackageResources.reader(TEST_FILE, StandardCharsets.UTF_8)) {
      assertNotNull(reader, "Reader should not be null");
      char[] buffer = new char[EXPECTED_CONTENT.length()];
      int read = reader.read(buffer);
      assertEquals(EXPECTED_CONTENT.length(), read);
      assertEquals(EXPECTED_CONTENT, new String(buffer));
    }
  }

  @Test
  void lines_ValidResource_ReturnsStreamOfLines() {
    try (Stream<String> lines = PackageResources.lines(TEST_FILE, StandardCharsets.UTF_8)) {
      List<String> collected = lines.collect(Collectors.toList());
      assertEquals(1, collected.size());
      assertEquals(EXPECTED_CONTENT, collected.get(0));
    }
  }
}
