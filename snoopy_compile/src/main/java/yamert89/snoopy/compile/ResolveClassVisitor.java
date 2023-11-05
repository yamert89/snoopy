package yamert89.snoopy.compile;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;
import yamert89.snoopy.compile.meta.Descriptors;

public class ResolveClassVisitor extends ClassVisitor {
    private boolean isTarget = false;

    protected ResolveClassVisitor(int api) {
        super(api);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        if (descriptor.equals(Descriptors.REPLACE_SQL)) setAsTarget();
        return super.visitAnnotation(descriptor, visible);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        return new FieldVisitor(Opcodes.ASM9) {
            @Override
            public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
                if (descriptor.equals(Descriptors.MAPPER)) setAsTarget();
                return super.visitAnnotation(descriptor, visible);
            }
        };
    }

    private void setAsTarget(){
        isTarget = true;
        visitEnd();
    }

    public boolean isTarget(){
        return isTarget;
    }
}
