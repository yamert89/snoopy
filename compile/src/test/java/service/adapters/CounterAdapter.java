package service.adapters;

import org.objectweb.asm.ClassVisitor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.objectweb.asm.Opcodes.ASM9;

public abstract class CounterAdapter extends ClassVisitor {
    protected int counter;
    protected final int expected;

    protected CounterAdapter(int expected) {
        super(ASM9);
        this.expected = expected;
    }

    @Override
    public void visitEnd() {
        assertEquals(expected, counter);
        super.visitEnd();
    }
}
