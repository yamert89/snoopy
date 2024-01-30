package yamert89.snoopy.compile.adapters;

import org.objectweb.asm.MethodVisitor;
import yamert89.snoopy.compile.ClassField;

import static yamert89.snoopy.compile.Constants.API_VERSION;

public class GetterAdapter extends MethodVisitor {
    private final ClassField classField;

    protected GetterAdapter(ClassField classField, MethodVisitor methodVisitor) {
        super(API_VERSION, methodVisitor);
        this.classField = classField;
    }

    @Override
    public void visitLdcInsn(Object value) {
        super.visitLdcInsn(classField.getNewValue());
    }

}
