package ru.iskhakov.util;

import lombok.experimental.UtilityClass;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@UtilityClass
public class FileInputReaderUtil {

    public static String read(String path) throws IOException {
        String fileContent = "";
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(path), StandardCharsets.UTF_8))) {
            String sub;
            while ((sub = br.readLine()) != null) {
                fileContent = String.format("%s%s\n", fileContent, sub);
            }
        }
        return fileContent;
    }
}
