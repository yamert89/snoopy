package yamert89.snoopy.compile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.Set;

public class ClassScanner {
    private final String classDir;
    private final Set<String> classFiles = new HashSet<>();
    private final Logger log = LoggerFactory.getLogger(ClassScanner.class);
    public ClassScanner(String classDir) {
        this.classDir = classDir;
    }

    public Set<String> scan() throws IOException {
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
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                if (!file.toFile().getName().endsWith(".class")) return FileVisitResult.CONTINUE;
                String path = file.toFile().getAbsolutePath();
                classFiles.add(path);
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
