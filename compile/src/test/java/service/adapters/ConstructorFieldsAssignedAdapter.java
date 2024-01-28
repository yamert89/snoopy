package service.adapters;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import yamert89.snoopy.compile.meta.Descriptors;
import yamert89.snoopy.meta.InjectSQL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static yamert89.snoopy.compile.Constants.API_VERSION;

/**
 * Adapter for classes marked as {@link InjectSQL}
 * */
public class ConstructorFieldsAssignedAdapter extends ClassVisitor {
    private final String fieldName;
    private final String expectedValue;

    public ConstructorFieldsAssignedAdapter(String fieldName, String expectedValue) {
        super(API_VERSION);
        this.fieldName = fieldName;
        this.expectedValue = expectedValue;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        if (name.equals(Descriptors.INIT)){
            return new MethodVisitor(API_VERSION) {
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
