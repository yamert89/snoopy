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

    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
        if (opcode == PUTFIELD
                && descriptor.equals(Descriptors.STRING)
                && !classFields.isEmpty()) {
            ClassField classField = classFields.poll();

            while (classField != null && !classField.getName().equals(name)) {
                if (!classField.isTarget()) {
                    if (!classField.isInitialized()) {
                        classField = classFields.poll();
                        continue;
                    }
                    super.visitLdcInsn(cachedLdcValue);
                    super.visitFieldInsn(opcode, owner, name, descriptor);
                    return;
                }
                super.visitLdcInsn(classField.getNewValue());
                super.visitFieldInsn(PUTFIELD, owner, classField.getName(), Descriptors.STRING);
                classField = classFields.poll();
                super.visitVarInsn(ALOAD, 0);
            }

            if (classField != null && classField.isTarget()) {
                super.visitLdcInsn(classField.getNewValue());
                super.visitFieldInsn(opcode, owner, name, descriptor);
            } else {
                super.visitLdcInsn(cachedLdcValue);
                super.visitFieldInsn(opcode, owner, name, descriptor);
            }

        } else {
            super.visitFieldInsn(opcode, owner, name, descriptor);
        }
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
        if (opcode == INVOKESPECIAL) {
            ClassField classField = classFields.peek();
            while (!classFields.isEmpty() && classField.isTarget() && !classField.isInitialized()) {
                classFields.remove();
                initializeField(CLASS_INTERNAL_NAME, classField.getName(), classField.getNewValue());
                classField = classFields.peek();
            }
        }
    }

    @Override
    public void visitInsn(int opcode) {
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
