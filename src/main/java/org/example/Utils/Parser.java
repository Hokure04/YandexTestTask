package org.example.Utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Path;

public class Parser {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static <T> T parse(Path path, Class<T> clazz) {
        try{
            return OBJECT_MAPPER.readValue(path.toFile(), clazz);
        } catch (IOException e){
            throw new RuntimeException("Невозможно распарсить файл" + path, e);
        }

    }
}
