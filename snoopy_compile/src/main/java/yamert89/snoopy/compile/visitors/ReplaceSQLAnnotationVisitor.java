package yamert89.snoopy.compile.visitors;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Opcodes;

public class ReplaceSQLAnnotationVisitor extends AnnotationVisitor {
    protected ReplaceSQLAnnotationVisitor() {
        super(Opcodes.ASM9);
    }

    @Override
    public void visit(String name, Object value) {
        super.visit(name, value);
    }
}
