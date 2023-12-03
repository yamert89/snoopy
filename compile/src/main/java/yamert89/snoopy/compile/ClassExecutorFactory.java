package yamert89.snoopy.compile;

import org.objectweb.asm.ClassReader;

public interface ClassExecutorFactory {
    ClassExecutor build(ClassReader reader, String originalPath, ClassMetadata classMetadata);
}
