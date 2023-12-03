package yamert89.snoopy.compile;

import org.objectweb.asm.ClassReader;

public class CopyTargetsClassExecutorFactory implements ClassExecutorFactory {
    @Override
    public ClassExecutor build(ClassReader reader, String originalPath, ClassMetadata classMetadata) {
        return new CopyTargetsClassExecutor(reader, originalPath, classMetadata);
    }
}
