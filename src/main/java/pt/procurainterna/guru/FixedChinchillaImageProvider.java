package pt.procurainterna.guru;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FixedChinchillaImageProvider implements ChinchillaImageProvider {

    @Override
    public InputStream getImage(){
        Path chinchillaPath = Paths.get("./test/chinchilla.jpg");

        try {
            return Files.newInputStream(chinchillaPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
