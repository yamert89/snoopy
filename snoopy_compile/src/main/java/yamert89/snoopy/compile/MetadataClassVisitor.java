package yamert89.snoopy.compile;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;
import yamert89.snoopy.compile.meta.Descriptors;
import yamert89.snoopy.compile.visitors.ReadReplaceSqlAnnotationVisitor;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class MetadataClassVisitor extends ClassVisitor {
    private boolean isTarget = false;

    private final ReadReplaceSqlAnnotationVisitor annotationVisitor = new ReadReplaceSqlAnnotationVisitor(Opcodes.ASM9);

    public MetadataClassVisitor(int api) {
        super(api);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        if (descriptor.equals(Descriptors.REPLACE_SQL)) {
            setAsTarget();
            return annotationVisitor;
        }
        return super.visitAnnotation(descriptor, visible);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        return new FieldVisitor(Opcodes.ASM9) {
            @Override
            public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
                if (descriptor.equals(Descriptors.REPLACE_SQL_FIELD)) {
                    setAsTarget();
                }
                return super.visitAnnotation(descriptor, visible);
            }
        };
    }

    private void setAsTarget(){
        isTarget = true;
        visitEnd();
    }

    public ClassMetadata getClassMetadata() {
        Supplier<String> targetFieldsPrefixFun = annotationVisitor.getPrefixFun();
        return new ClassMetadata(isTarget, targetFieldsPrefixFun == null ? null : targetFieldsPrefixFun.get());
    }
}
