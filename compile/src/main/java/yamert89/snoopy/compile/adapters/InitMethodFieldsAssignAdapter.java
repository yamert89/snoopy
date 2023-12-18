package yamert89.snoopy.compile.adapters;

import org.objectweb.asm.MethodVisitor;
import yamert89.snoopy.compile.ClassField;
import yamert89.snoopy.compile.meta.Descriptors;

import java.util.LinkedList;

import static org.objectweb.asm.Opcodes.*;

public class InitMethodFieldsAssignAdapter extends MethodVisitor {
    private final LinkedList<ClassField> classFields;
    private Object cachedLdcValue;
    private final String CLASS_INTERNAL_NAME;

    protected InitMethodFieldsAssignAdapter(LinkedList<ClassField> classFields, String className, int api, MethodVisitor methodVisitor) {
        super(api, methodVisitor);
        assert !classFields.isEmpty();
        this.classFields = classFields;
        CLASS_INTERNAL_NAME = className;
    }

    @Override
    public void visitLdcInsn(Object value) {
        if (value instanceof String) {
            cachedLdcValue = value;
        } else super.visitLdcInsn(value);
    }

   /* @Override
    public void visitVarInsn(int opcode, int varIndex) {
        if (opcode == ALOAD && varIndex == 0) return;
        super.visitVarInsn(opcode, varIndex);
    }*/

    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
        if (opcode == PUTFIELD
                && descriptor.equals(Descriptors.STRING)) {
            ClassField classField;

            do {
                classField = classFields.poll();
                if (classField.isTarget()) {
                    super.visitLdcInsn(classField.getNewValue());
                    super.visitFieldInsn(opcode, owner, name, descriptor);
                } else {
                    super.visitLdcInsn(cachedLdcValue);
                    super.visitFieldInsn(opcode, owner, name, descriptor);
                }

            } while (classField != null && !classField.getName().equals(name));

        } else {
            super.visitFieldInsn(opcode, owner, name, descriptor);
        }
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
        if (opcode == INVOKESPECIAL) {
            ClassField classField = classFields.peek();
            if (classField.isTarget() && !classField.isInitialized()) classField = classFields.poll();
            while (classField.isTarget() && !classField.isInitialized()) {
                initializeField(CLASS_INTERNAL_NAME, classField.getName(), classField.getNewValue());
                classField = classFields.poll();
            }
        }
    }

    @Override
    public void visitInsn(int opcode) {  //todo add initContainsPutfield
        if (opcode == RETURN) {
            classFields.stream().filter(ClassField::isTarget).forEach(classField -> initializeField(
                    CLASS_INTERNAL_NAME,
                    classField.getName(),
                    classField.getNewValue())
            );
        }
        super.visitInsn(opcode);
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
    }

    private void initializeField(String owner, String name, Object value) {
        super.visitVarInsn(ALOAD, 0);
        super.visitLdcInsn(value);
        super.visitFieldInsn(PUTFIELD, owner, name, Descriptors.STRING);
    }

}
