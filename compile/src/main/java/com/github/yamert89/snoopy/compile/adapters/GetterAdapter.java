package com.github.yamert89.snoopy.compile.adapters;

import com.github.yamert89.snoopy.compile.ClassField;
import org.objectweb.asm.MethodVisitor;

import static com.github.yamert89.snoopy.compile.Constants.API_VERSION;

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
