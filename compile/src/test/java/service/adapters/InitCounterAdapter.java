package service.adapters;

import com.github.yamert89.snoopy.compile.meta.Descriptors;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.objectweb.asm.Opcodes.*;

public class InitCounterAdapter extends ClassVisitor {
    private final int expected;
    private int aload0Counter;
    private int ldcStringCounter;
    private int putFieldCounter;

    public InitCounterAdapter(int expected) {
        super(ASM9);
        this.expected = expected;
        aload0Counter = -1; //aload0 invokespecial -> aload0
        ldcStringCounter = 0;
        putFieldCounter = 0;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        if (name.equals(Descriptors.INIT)) {
            return new MethodVisitor(super.api) {

                @Override
                public void visitVarInsn(int opcode, int varIndex) {
                    super.visitVarInsn(opcode, varIndex);
                    if (opcode == ALOAD && varIndex == 0) aload0Counter++;
                }

                @Override
                public void visitLdcInsn(Object value) {
                    super.visitLdcInsn(value);
                    if (value instanceof String) ldcStringCounter++;
                }

                @Override
                public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
                    super.visitFieldInsn(opcode, owner, name, descriptor);
                    if (opcode == PUTFIELD && descriptor.equals(Descriptors.STRING)) putFieldCounter++;
                }
            };
        }
        return super.visitMethod(access, name, descriptor, signature, exceptions);
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
        assertEquals(expected, aload0Counter, "Unexpected aload0Counter");
        assertEquals(expected, ldcStringCounter, "Unexpected ldcStringCounter");
        assertEquals(expected, putFieldCounter, "Unexpected putFieldCounter");
    }
}
