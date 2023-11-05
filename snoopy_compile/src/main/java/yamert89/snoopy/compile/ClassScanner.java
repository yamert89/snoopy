package yamert89.snoopy.compile;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.Set;

public class ClassScanner {
    private final String basePath;
    private final String rootPath;
    private final Set<String> classFiles = new HashSet<>();
    public ClassScanner(String rootPath, String basePath) {
        this.basePath = basePath;
        this.rootPath = rootPath;
        System.out.println("Class Scanner root: " + rootPath);
    }

    public Set<String> scan() throws IOException {
        collectClasses();
        return classFiles;
    }

    private void collectClasses() throws IOException {
        Files.walkFileTree(Paths.get(rootPath + "/" + basePath), new FileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                System.out.println("basePath: " + basePath);
                System.out.println(file.toAbsolutePath());
                if (!file.toFile().getName().endsWith(".class")) return FileVisitResult.CONTINUE;
                classFiles.add(file.toFile().getAbsolutePath());
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
                System.out.println("visit " + dir.toAbsolutePath());
                return FileVisitResult.CONTINUE;
            }
        });
    }
}
