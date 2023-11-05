package yamert89.snoopy.compile;

import org.objectweb.asm.ClassReader;

public interface ClassAcceptor {
    boolean accepted(ClassReader reader);
}
