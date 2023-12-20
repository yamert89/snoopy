package yamert89.snoopy.compile.adapters;

import org.objectweb.asm.MethodVisitor;
import yamert89.snoopy.compile.ClassField;

public class GetterAdapter extends MethodVisitor {
    private final ClassField classField;

    protected GetterAdapter(ClassField classField, int api, MethodVisitor methodVisitor) {
        super(api, methodVisitor);
        this.classField = classField;
    }

    @Override
    public void visitLdcInsn(Object value) {
        super.visitLdcInsn(classField.getNewValue());
    }

    /*@Override
    public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
        if (opcode == GETFIELD)
        super.visitFieldInsn(opcode, owner, name, descriptor);
    }*/
}
