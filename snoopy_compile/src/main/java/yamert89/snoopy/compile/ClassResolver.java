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
        //todo add annotation scanning
        try{
            System.out.println("Resolve: " + classFilePath);
            var is = new FileInputStream(classFilePath);
            var reader = new ClassReader(is);

            is.close();
        } catch (Exception e){
            e.printStackTrace();
        }


    }
}
