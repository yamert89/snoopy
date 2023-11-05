package yamert89.snoopy.compile;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;

public class InjectSqlClassAcceptor implements ClassAcceptor {
    @Override
    public boolean accepted(ClassReader reader) {
        ResolveClassVisitor resolveClassVisitor = new ResolveClassVisitor(Opcodes.ASM9);
        reader.accept(resolveClassVisitor, 0);
        return resolveClassVisitor.isTarget();
    }
}
