package pt.procurainterna.guru;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FixedChinchillaImageProvider implements ChinchillaImageProvider {

  @Override
  public URI getImageURI() {
    try {
      return new URI("./test/chinchilla.jpg");
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public InputStream getImage() {
    Path chinchillaPath = Paths.get("./test/chinchilla.jpg");

    try {
      return Files.newInputStream(chinchillaPath);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
