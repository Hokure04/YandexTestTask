package org.example.Utils;

import java.nio.file.Path;
import java.nio.file.Paths;

public class PathManager {

    private static final String folder = "src/main/resources";

    public static String getPath(String filename){
        String baseDir = System.getProperty("user.dir");
        Path path = Paths.get(baseDir, folder, filename);
        return path.toAbsolutePath().normalize().toString();
    }
}
