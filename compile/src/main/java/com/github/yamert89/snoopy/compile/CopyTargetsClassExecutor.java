package com.github.yamert89.snoopy.compile;

import com.github.yamert89.snoopy.compile.adapters.TargetClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.util.CheckClassAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private final File classFile;
    private final ClassMetadata classMetadata;
    private final Logger log = LoggerFactory.getLogger(InjectSqlExecutor.class);

    public CopyTargetsClassExecutor(ClassReader reader, File classFile, ClassMetadata classMetadata) {
        this.reader = reader;
        this.classFile = classFile;
        this.classMetadata = classMetadata;
    }
    @Override
    public void run() {
        try{
            if (classFile.getAbsolutePath().contains("converted")) return;
            log.debug("execute file: {}", classFile);
            var writer = new ClassWriter(reader, ClassWriter.COMPUTE_MAXS);
            TargetClassAdapter targetClassAdapter = new TargetClassAdapter(new CheckClassAdapter(writer), classMetadata);
            reader.accept(targetClassAdapter, 0);
            var bytes = writer.toByteArray();
            var path = classFile.toPath();
            var fileName = path.getFileName();
            Path converted = path.getParent().resolve("converted/");
            if (!converted.toFile().exists()) Files.createDirectory(converted);
            Files.walkFileTree(converted, new FileVisitor<>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
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
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
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
