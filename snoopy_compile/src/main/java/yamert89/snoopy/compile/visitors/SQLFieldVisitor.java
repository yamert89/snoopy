package yamert89.snoopy.compile.visitors;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;

public class SQLFieldVisitor extends FieldVisitor {
    protected SQLFieldVisitor(FieldVisitor fieldVisitor) {
        super(Opcodes.ASM9, fieldVisitor);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        return new MapperAnnotationVisitor(super.visitAnnotation(descriptor, visible));
    }


}
