package yamert89.snoopy.compile;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yamert89.snoopy.compile.visitors.MetadataClassVisitor;

import java.io.FileInputStream;

public final class ClassResolver {
    private final ClassExecutorFactory classExecutorFactory;
    private final String classFilePath;
    private final Logger log = LoggerFactory.getLogger(ClassResolver.class);

    public ClassResolver(String classFilePath, ClassExecutorFactory classExecutorFactory) {
        this.classExecutorFactory = classExecutorFactory;
        this.classFilePath = classFilePath;
    }

    public ClassResolver(String classFilePath) {
        this.classFilePath = classFilePath;
        classExecutorFactory = new InjectSqlClassExecutorFactory();
    }

    public void resolve(){
        try{
            var is = new FileInputStream(classFilePath);
            var reader = new ClassReader(is);
            is.close();
            ClassMetadata clMetadata = getMetadata(reader);
            log.debug("resolved class metadata: {}", clMetadata);
            if (clMetadata.isTarget()){
                ClassExecutor classExecutor = classExecutorFactory.build(reader, classFilePath, clMetadata);
                classExecutor.run();
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
