package yamert89.snoopy.compile;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;

import java.io.FileInputStream;

public final class ClassResolver {
    public void resolve(String classFilePath){

        try(var is = new FileInputStream(classFilePath);){
            System.out.println("Resolve: " + classFilePath);
            var reader = new ClassReader(is);
            ClassMetadata clMetadata = getMetadata(reader);
            if (clMetadata.isTarget()){
                InjectSqlExecutor injectSqlExecutor = new InjectSqlExecutor(reader, classFilePath, clMetadata);
                injectSqlExecutor.run();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private ClassMetadata getMetadata(ClassReader reader){
        MetadataClassVisitor metadataClassVisitor = new MetadataClassVisitor(Opcodes.ASM9);
        reader.accept(metadataClassVisitor, 0);
        return metadataClassVisitor.getClassMetadata();
    }


}
