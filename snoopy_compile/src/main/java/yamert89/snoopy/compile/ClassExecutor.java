package yamert89.snoopy.compile;

import org.objectweb.asm.ClassReader;

public interface ClassExecutor {
    void run(ClassReader reader, String originalPath);
}
