package io.github.edsonisaac.jogomemoriabackend.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class FileUtils {
    public static final Map<String, MultipartFile> FILES = new HashMap<>();
    public static final String IMAGES_DIRECTORY = File.separator + "data" + File.separator + "files" + File.separator + "images";

    public static File find(String filename, String path) throws FileNotFoundException {
        final var file = new File(System.getProperty("user.dir") + path, filename);
        if (!file.exists()) {
            throw new FileNotFoundException(MessageUtils.FILE_NOT_FOUND);
        }
        return file;
    }

    public static File save(MultipartFile file, String path) throws IOException {
        final var extension = getExtension(Objects.requireNonNull(file.getOriginalFilename()));
        final var filename = System.currentTimeMillis() + "." + extension;
        final var filePath = Paths.get(System.getProperty("user.dir") + path, filename);
        Files.write(filePath, file.getBytes());
        return find(filename, path);
    }

    public static File save(String filename, MultipartFile file, String path) throws IOException {
        final var filePath = Paths.get(System.getProperty("user.dir") + path, filename);
        Files.write(filePath, file.getBytes());
        return find(filename, path);
    }

    public static boolean delete(String filename, String path) {
        final var file = new File(System.getProperty("user.dir") + path, filename);
        return file.exists() && file.isFile() && file.delete();
    }

    public static String getExtension(String filename) {
        final var fileNameParts = filename.split("\\.");
        return fileNameParts.length > 1 ? fileNameParts[fileNameParts.length - 1] : "";
    }
}
