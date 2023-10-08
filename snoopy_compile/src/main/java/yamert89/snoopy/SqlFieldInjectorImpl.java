package yamert89.snoopy;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class SqlFieldInjectorImpl implements SqlFieldInjector {
    @Override
    public void run() {
        try{
            var is = new FileInputStream("snoopy_compile/build/classes/java/main/yamert89/snoopy/Simple.class");
            var reader = new ClassReader(is);
            var writer = new ClassWriter(reader, 0);
            InjectFieldVisitor injectFieldVisitor = new InjectFieldVisitor(Opcodes.ASM9, writer);
            reader.accept(injectFieldVisitor, 0);
            /*var bytes = writer.toByteArray();
            var file = new File("snoopy_compile/build/classes/java/main/yamert89/snoopy/Simple.class");
            is.close();
            file.delete();
            file.createNewFile();
            var out = new FileOutputStream(file);
            out.write(bytes);
            out.close();*/
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
