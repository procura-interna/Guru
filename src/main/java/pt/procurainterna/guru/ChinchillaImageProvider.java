package pt.procurainterna.guru;

import java.io.InputStream;
import java.net.URI;

public interface ChinchillaImageProvider {
  URI getImageURI();

  InputStream getImage();
}
