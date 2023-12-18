package yamert89.snoopy.compile.adapters;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.FieldVisitor;
import yamert89.snoopy.compile.meta.Descriptors;

import java.util.function.Supplier;

public class SingleFieldAdapter extends FieldVisitor {
    private boolean fieldIsTarget;
    private final Supplier<Boolean> targetFunc;
    private final Supplier<Boolean> notTargetFunc;

    protected SingleFieldAdapter(int api, Supplier<Boolean> targetFunc, Supplier<Boolean> notTargetFunc) {
        super(api);
        fieldIsTarget = false;
        this.targetFunc = targetFunc;
        this.notTargetFunc = notTargetFunc;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        if (descriptor.equals(Descriptors.REPLACE_SQL_FIELD)) {
            targetFunc.get();
            fieldIsTarget = true;
        }
        return super.visitAnnotation(descriptor, visible);
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
        if (!fieldIsTarget) {
            notTargetFunc.get();
        }
    }
}
