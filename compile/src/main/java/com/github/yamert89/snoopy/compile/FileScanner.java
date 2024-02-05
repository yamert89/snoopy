package com.github.yamert89.snoopy.compile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedList;
import java.util.List;

public class FileScanner {
    private final String directory;
    private final String extension;
    private final List<File> files = new LinkedList<>();
    private final Logger log = LoggerFactory.getLogger(FileScanner.class);

    public FileScanner(String directory, String extension) {
        this.directory = directory;
        this.extension = "." + extension;
    }

    public List<File> scan() throws IOException {
        log.debug("scan in root: {}", directory);
        collectFiles();
        return files;
    }

    private void collectFiles() throws IOException {
        Files.walkFileTree(Paths.get(directory), new FileVisitor<>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) {
                if (!path.toFile().getName().endsWith(extension)) return FileVisitResult.CONTINUE;
                files.add(path.toFile());
                log.debug("file found: {}", path);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
                return FileVisitResult.CONTINUE;
            }
        });
    }
}
