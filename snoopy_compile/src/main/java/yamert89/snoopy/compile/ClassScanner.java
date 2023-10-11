package yamert89.snoopy.compile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ClassScanner {
    private final String basePath;
    private final String rootPath;
    private final Set<String> classFiles = new HashSet<>();
    public ClassScanner(String basePath, String rootPath) {
        this.basePath = basePath;
        this.rootPath = rootPath;
    }

    public void scanAndHandle() throws IOException {
        collectClasses();
        ClassPatcher patcher = new ClassPatcherImpl();
        classFiles.forEach(patcher::run);
    }

    private void collectClasses() throws IOException {
        Files.walkFileTree(Paths.get(rootPath + "/" + basePath), new FileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                String fileName = file.toString().replace("\\", "/");
                var className =  fileName.substring(fileName.indexOf(basePath))
                        .replace("/", ".")
                        .replace(".class", "");
                try {
                    Class<?> aClass = Class.forName(className, false, this.getClass().getClassLoader());
                    if (aClass.isAnnotationPresent(ReplaceSql.class)) classFiles.add(file.toFile().getAbsolutePath());
                    else if (Arrays.stream(aClass.getDeclaredFields()).anyMatch(field ->
                            field.isAnnotationPresent(Mapper.class))) classFiles.add(file.toFile().getAbsolutePath());
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
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
