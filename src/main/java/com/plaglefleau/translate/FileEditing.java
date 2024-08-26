package com.plaglefleau.translate;

import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Utility class for file operations and JSON handling.
 */
public class FileEditing {

    private static final Gson GSON = new Gson();

    /**
     * Reads a JSON file and converts it to an object of the specified type.
     *
     * @param path        the path to the JSON file
     * @param objectClass the type of the object to deserialize
     * @param <T>         the type of the object
     * @return the deserialized object or null if the file does not exist
     * @throws IOException if an I/O error occurs while reading the file
     */
    public static <T> @Nullable T readJsonFile(@NotNull Path path, Type objectClass) throws IOException {
        if (!Files.exists(path)) {
            return null;
        }

        AtomicReference<String> atomicReference = new AtomicReference<>("");

        Files.readAllLines(path, Charset.defaultCharset()).forEach(line -> atomicReference.set(atomicReference.get() + line));

        String json = atomicReference.get();

        return GSON.fromJson(json, objectClass);
    }

    /**
     * Writes an object to a JSON file.
     *
     * @param path   the path to the JSON file
     * @param object the object to serialize and write to the file
     * @param <T>    the type of the object
     * @throws IOException if an I/O error occurs while writing the file
     */
    public static <T> void writeJsonFile(@NotNull Path path, @NotNull T object) throws IOException {
        Files.createDirectories(path.getParent());
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
        Files.write(path, GSON.toJson(object).getBytes());
    }

    /**
     * Gets the file names in a folder with a specified extension.
     *
     * @param folderPath the path to the folder
     * @param extension  the file extension to filter by (can be null for all files)
     * @return a list of file names without extension
     */
    public static @NotNull List<String> getFolderFileNames(@NotNull String folderPath, String extension) {
        List<String> fileNames = new ArrayList<>();
        File folder = new File(folderPath);

        if (!folder.exists() || !folder.isDirectory()) {
            return fileNames;
        }

        File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    String fileName = file.getName();
                    if (extension == null || extension.isEmpty() || fileName.endsWith("." + extension)) {
                        // Remove extension from the file name
                        int dotIndex = fileName.lastIndexOf('.');
                        if (dotIndex > 0) {
                            fileNames.add(fileName.substring(0, dotIndex));
                        } else {
                            fileNames.add(fileName);
                        }
                    }
                }
            }
        }

        return fileNames;
    }

    /**
     * Gets the file names in a folder without filtering by extension.
     *
     * @param folderPath the path to the folder
     * @return a list of all file names in the folder without extension
     */
    public static @NotNull List<String> getFolderFileNames(@NotNull String folderPath) {
        return getFolderFileNames(folderPath, null);
    }
}
