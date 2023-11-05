package yamert89.snoopy.compile;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;

public final class ClassResolver {
    public void resolve(String classFilePath){

        try(var is = new FileInputStream(classFilePath);){
            System.out.println("Resolve: " + classFilePath);
            var reader = new ClassReader(is);
            if (isTarget(reader)){
                InjectSqlExecutor injectSqlExecutor = new InjectSqlExecutor();
                injectSqlExecutor.run(reader, classFilePath);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private boolean isTarget(ClassReader reader){
        return new InjectSqlClassAcceptor().accepted(reader);
    }
}
