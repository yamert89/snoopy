package yamert89.snoopy.compile;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Opcodes;

public class ReplaceSQLAnnotationVisitor extends AnnotationVisitor {
    protected ReplaceSQLAnnotationVisitor() {
        super(Opcodes.ASM9);
    }

    @Override
    public void visit(String name, Object value) {
        System.out.println("visit replaceSqlAnnotationVisitor: name=" + name + ", value=" + value);
        super.visit(name, value);
    }
}