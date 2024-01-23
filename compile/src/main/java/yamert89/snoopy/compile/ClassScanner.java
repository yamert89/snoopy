package yamert89.snoopy.compile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedList;
import java.util.List;

public class ClassScanner {
    private final String classDir;
    private final List<File> classFiles = new LinkedList<>();
    private final Logger log = LoggerFactory.getLogger(ClassScanner.class);
    public ClassScanner(String classDir) {
        this.classDir = classDir;
    }

    public List<File> scan() throws IOException {
        log.debug("scan in root: {}", classDir);
        collectClasses();
        return classFiles;
    }

    private void collectClasses() throws IOException {
        Files.walkFileTree(Paths.get(classDir), new FileVisitor<>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) {
                if (!path.toFile().getName().endsWith(".class")) return FileVisitResult.CONTINUE;
                classFiles.add(path.toFile());
                log.debug("found class: {}", path);
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
