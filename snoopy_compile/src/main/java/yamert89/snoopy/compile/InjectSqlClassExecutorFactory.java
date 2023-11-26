package yamert89.snoopy.compile;

import org.objectweb.asm.ClassReader;

public class InjectSqlClassExecutorFactory implements ClassExecutorFactory {
    @Override
    public ClassExecutor build(ClassReader reader, String originalPath, ClassMetadata classMetadata) {
        return new InjectSqlExecutor(reader, originalPath, classMetadata);
    }
}
