package yamert89.snoopy.compile;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yamert89.snoopy.compile.adapters.TargetClassAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;

public class InjectSqlExecutor implements ClassExecutor {
    private final ClassReader reader;
    private final String originalPath;
    private final ClassMetadata classMetadata;
    private final Logger log = LoggerFactory.getLogger(InjectSqlExecutor.class);

    public InjectSqlExecutor(ClassReader reader, String originalPath, ClassMetadata classMetadata) {
        this.reader = reader;
        this.originalPath = originalPath;
        this.classMetadata = classMetadata;
    }

    @Override
    public void run() {
        try{
            log.debug("execute path: {}", originalPath);
            var writer = new ClassWriter(reader, ClassWriter.COMPUTE_MAXS);
            TargetClassAdapter targetClassAdapter = new TargetClassAdapter(Opcodes.ASM9, writer, classMetadata);
            reader.accept(targetClassAdapter, 0);
            var bytes = writer.toByteArray();
            var file = new File(originalPath);
            //is.close();
            var path = file.toPath();
            Files.delete(path);
            Files.createFile(path);
            var out = new FileOutputStream(file);
            out.write(bytes);
            out.close();
            log.debug("saved file {}", path);
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
    }
}
