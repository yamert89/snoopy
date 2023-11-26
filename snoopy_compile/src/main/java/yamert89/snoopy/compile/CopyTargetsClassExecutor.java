package yamert89.snoopy.compile;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yamert89.snoopy.compile.visitors.TargetClassVisitor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public class CopyTargetsClassExecutor implements ClassExecutor {

    private final ClassReader reader;
    private final String originalPath;
    private final ClassMetadata classMetadata;
    private final Logger log = LoggerFactory.getLogger(InjectSqlExecutor.class);

    public CopyTargetsClassExecutor(ClassReader reader, String originalPath, ClassMetadata classMetadata) {
        this.reader = reader;
        this.originalPath = originalPath;
        this.classMetadata = classMetadata;
    }
    @Override
    public void run() {
        try{
            if (originalPath.contains("converted")) return;
            log.debug("execute path: {}", originalPath);
            var writer = new ClassWriter(reader, 0);
            TargetClassVisitor targetClassVisitor = new TargetClassVisitor(Opcodes.ASM9, writer, classMetadata);
            reader.accept(targetClassVisitor, 0);
            var bytes = writer.toByteArray();
            var file = new File(originalPath);
            var path = file.toPath();
            var fileName = path.getFileName();
            Path converted = path.getParent().resolve("converted/");
            if (!converted.toFile().exists()) Files.createDirectory(converted);
            Files.walkFileTree(converted, new FileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (file.getFileName().equals(path.getFileName())) {
                        boolean res = Files.deleteIfExists(file);
                        if (!res) throw new IllegalStateException("File not deleted");
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    throw exc;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }
            });
            Path file1 = Files.createFile(path.getParent().resolve("converted/" + fileName));
            var out = new FileOutputStream(file1.toFile());
            out.write(bytes);
            out.close();
            log.debug("saved file {}", path);
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
    }
}
