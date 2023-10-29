package yamert89.snoopy.compile;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;

public class InjectSqlExecutor implements ClassExecutor {
    @Override
    public void run(ClassReader reader, String originalPath) {
        try{
            var writer = new ClassWriter(reader, 0);
            InjectFieldVisitor injectFieldVisitor = new InjectFieldVisitor(Opcodes.ASM9, writer);
            reader.accept(injectFieldVisitor, 0);
            var bytes = writer.toByteArray();
            var file = new File(originalPath);
            //is.close();
            var path = file.toPath();
            Files.delete(path);
            Files.createFile(path);
            var out = new FileOutputStream(file);
            out.write(bytes);
            out.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
