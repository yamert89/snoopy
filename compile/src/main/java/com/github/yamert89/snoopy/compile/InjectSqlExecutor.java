package com.github.yamert89.snoopy.compile;

import com.github.yamert89.snoopy.compile.adapters.TargetClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;

public class InjectSqlExecutor implements ClassExecutor {
    private final ClassReader reader;
    private final File classFile;
    private final ClassMetadata classMetadata;
    private final Logger log = LoggerFactory.getLogger(InjectSqlExecutor.class);

    public InjectSqlExecutor(ClassReader reader, File classFile, ClassMetadata classMetadata) {
        this.reader = reader;
        this.classFile = classFile;
        this.classMetadata = classMetadata;
    }

    @Override
    public void run() {
        try{
            log.debug("execute file: {}", classFile);
            var writer = new ClassWriter(reader, ClassWriter.COMPUTE_MAXS);
            TargetClassAdapter targetClassAdapter = new TargetClassAdapter(writer, classMetadata);
            reader.accept(targetClassAdapter, 0);
            var bytes = writer.toByteArray();
            var path = classFile.toPath();
            Files.delete(path);
            Files.createFile(path);
            var out = new FileOutputStream(classFile);
            out.write(bytes);
            out.close();
            log.debug("saved file {}", path);
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
    }
}
