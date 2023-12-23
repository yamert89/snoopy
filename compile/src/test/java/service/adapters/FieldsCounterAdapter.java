package service.adapters;

import org.objectweb.asm.FieldVisitor;

public class FieldsCounterAdapter extends CounterAdapter {

    public FieldsCounterAdapter(int expected) {
        super(expected);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        counter++;
        return super.visitField(access, name, descriptor, signature, value);
    }
}
