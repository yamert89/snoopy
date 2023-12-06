package service.fakes;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import yamert89.snoopy.compile.meta.Descriptors;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Adapter for classes marked as {@link yamert89.snoopy.compile.meta.ReplaceSQL}
 * */
public class ConstructorFieldsAssignedAdapter extends ClassVisitor {
    private final String fieldName;
    private final String expectedValue;

    public ConstructorFieldsAssignedAdapter(int api, String fieldName, String expectedValue) {
        super(api);
        this.fieldName = fieldName;
        this.expectedValue = expectedValue;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        if (name.equals(Descriptors.INIT)){
            return new MethodVisitor(Opcodes.ASM9) {
                private String sqlValue;
                @Override
                public void visitLdcInsn(Object value) {
                    sqlValue = (String) value;
                    super.visitLdcInsn(value);
                }
                @Override
                public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
                    if (name.equals(fieldName) && opcode == Opcodes.PUTFIELD) assertEquals(expectedValue, sqlValue);
                    super.visitFieldInsn(opcode, owner, name, descriptor);
                }
            };
        }
        return super.visitMethod(access, name, descriptor, signature, exceptions);
    }
}
