package yamert89.snoopy.compile.adapters;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import yamert89.snoopy.compile.meta.Descriptors;

import java.util.Map;

public class InitMethodFieldsAssignAdapter extends MethodVisitor {
    private final Map<String, String> replacementFields;
    private String cachedString;
    protected InitMethodFieldsAssignAdapter(Map<String, String> replacementFields, int api, MethodVisitor methodVisitor) {
        super(api, methodVisitor);
        this.replacementFields = replacementFields;
    }

    @Override
    public void visitLdcInsn(Object value) {
        if (value instanceof String){
            cachedString = (String) value;
        } else {
            cachedString = null;
            super.visitLdcInsn(value);
        }
    }

    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
        boolean isString = descriptor.equals(Descriptors.STRING);
        if (opcode == Opcodes.PUTFIELD
                && isString
                && replacementFields.containsKey(name)){
            super.visitLdcInsn(replacementFields.get(name));
        } else if (isString) super.visitLdcInsn(cachedString);
        super.visitFieldInsn(opcode, owner, name, descriptor);
    }
}
