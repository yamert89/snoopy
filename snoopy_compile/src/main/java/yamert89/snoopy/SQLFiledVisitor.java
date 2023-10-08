package yamert89.snoopy;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;

public class SQLFiledVisitor extends FieldVisitor {
    protected SQLFiledVisitor(FieldVisitor fieldVisitor) {
        super(Opcodes.ASM9, fieldVisitor);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        System.out.println("Field annotation: " + descriptor);
        return new MapperAnnotationVisitor(super.visitAnnotation(descriptor, visible));
    }


}
