package org.example.Utils;

import java.nio.file.Path;
import java.nio.file.Paths;

public final class PathManager {

    private static final String RESOURCE_DIR = "src/main/resources";

    public static Path getPath(String filename){
        return Path.of(RESOURCE_DIR, filename).toAbsolutePath();
    }
}
