package yamert89.snoopy.compile.adapters;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yamert89.snoopy.compile.ClassField;
import yamert89.snoopy.compile.ClassMetadata;
import yamert89.snoopy.compile.meta.Descriptors;

import java.util.LinkedList;
import java.util.Optional;

import static org.objectweb.asm.Opcodes.*;


public class TargetClassAdapter extends ClassVisitor {
    private final ClassMetadata classMetadata;
    private String className;
    private final Logger log = LoggerFactory.getLogger(TargetClassAdapter.class);

    public TargetClassAdapter(int api, ClassVisitor cv, ClassMetadata classMetadata) {
        super(api, cv);
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
        if (optCF.isPresent()) {
            Object resultVal = access == ACC_PUBLIC + ACC_FINAL ? optCF.get().getNewValue() : value;
            return super.visitField(access, name, descriptor, signature, resultVal);
        }
        return super.visitField(access, name, descriptor, signature, value);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
        if (name.equals(Descriptors.INIT))
            return new InitMethodFieldsAssignAdapter((LinkedList<ClassField>) classMetadata.getClassFields(), className, ASM9, mv);
        return mv;
    }

}
