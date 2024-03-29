package com.github.yamert89.snoopy.compile.adapters;

import com.github.yamert89.snoopy.compile.ClassField;
import com.github.yamert89.snoopy.compile.ClassMetadata;
import com.github.yamert89.snoopy.compile.Constants;
import com.github.yamert89.snoopy.compile.meta.Descriptors;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.Optional;


public class TargetClassAdapter extends ClassVisitor {
    private final ClassMetadata classMetadata;
    private String className;
    private final Logger log = LoggerFactory.getLogger(TargetClassAdapter.class);

    public TargetClassAdapter(ClassVisitor cv, ClassMetadata classMetadata) {
        super(Constants.API_VERSION, cv);
        this.classMetadata = classMetadata;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        className = name;
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        log.debug("started analyzing field: {{}}", name);
        Optional<ClassField> optCF = classMetadata.getClassFields().stream().filter(f -> f.getName().equals(name)).findAny();
        if (descriptor.equals(Descriptors.STRING) && optCF.isPresent()) {
            Object resultVal = optCF.get().getNewValue();
            return super.visitField(access, name, descriptor, signature, resultVal);
        }
        return super.visitField(access, name, descriptor, signature, value);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
        if (name.equals(Descriptors.INIT))
            return new InitMethodFieldsAssignAdapter((LinkedList<ClassField>) classMetadata.getClassFields(), className, mv);

        String fieldName = name.replace("get", "");
        Optional<ClassField> optCF = classMetadata.getClassFields()
                .stream()
                .filter(f -> f.getName().equalsIgnoreCase(fieldName)).findAny(); //TODO fields with similar names (getField | getfield)
        if (name.startsWith("get") && optCF.isPresent())
            return new GetterAdapter(optCF.get(), mv);

        return mv;
    }

}
