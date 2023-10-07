package ru.iskhakov.util;

import lombok.experimental.UtilityClass;
import ru.iskhakov.exception.DomainException;

import javax.annotation.Nonnull;
import java.io.FileWriter;
import java.io.IOException;

@UtilityClass
public class FileOutputWriterUtil {

    private static final String OUTPUT_FILE_NAME = "output.json";

    public static void write(@Nonnull String output, @Nonnull String path) throws DomainException {
        int lastIndexOf = path.lastIndexOf("\\");
        String outputPath = path.substring(0, lastIndexOf + 1) + OUTPUT_FILE_NAME;
        try (FileWriter writer = new FileWriter(outputPath, false)) {
            writer.write(output);
            writer.flush();
        } catch (IOException ex) {
            throw new DomainException(ex.getMessage());
        }
    }
}
