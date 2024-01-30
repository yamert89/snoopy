package yamert89.snoopy.compile.adapters;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import yamert89.snoopy.compile.ClassField;
import yamert89.snoopy.compile.ClassMetadata;
import yamert89.snoopy.compile.meta.Descriptors;

import java.util.LinkedList;
import java.util.List;

import static org.objectweb.asm.Opcodes.PUTFIELD;
import static yamert89.snoopy.compile.Constants.API_VERSION;

public class ClassMetadataAdapter extends ClassVisitor {
    private final List<ClassField> classFields;
    private final ReadReplaceSqlAnnotationAdapter annotationVisitor;

    public ClassMetadataAdapter() {
        super(API_VERSION);
        classFields = new LinkedList<>();
        annotationVisitor = new ReadReplaceSqlAnnotationAdapter();
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        if (descriptor.equals(Descriptors.REPLACE_SQL)) {
            return annotationVisitor;
        }
        return super.visitAnnotation(descriptor, visible);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        if (!descriptor.equals(Descriptors.STRING)) return super.visitField(access, name, descriptor, signature, value);
        boolean fieldIsTargetByClassLevel = false;
        if (annotationVisitor.getPrefixFun() != null) {
            String targetFieldPrefix = annotationVisitor.getPrefixFun().get();
            if (name.startsWith(targetFieldPrefix)) fieldIsTargetByClassLevel = true;
        }
        return new SingleFieldAdapter(value, name, fieldIsTargetByClassLevel, classFields::add, annotationVisitor.getClassFilter());
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        if (name.equals(Descriptors.INIT) && !classFields.isEmpty()) {
            return new MethodVisitor(API_VERSION) {
                @Override
                public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
                    if (opcode == PUTFIELD) {
                        classFields.stream()
                                .filter(f -> f.getName().equals(name))
                                .findAny()
                                .ifPresent(classField -> classField.setInitialized(true));
                    }
                    super.visitFieldInsn(opcode, owner, name, descriptor);
                }
            };
        }
        return super.visitMethod(access, name, descriptor, signature, exceptions);
    }

    public ClassMetadata getClassMetadata() {
        return classFields.isEmpty() || classFields.stream().noneMatch(ClassField::isTarget) ?
                ClassMetadata.notTargetInstance()
                :
                annotationVisitor.getPrefixFun() != null ?
                        new ClassMetadata(annotationVisitor.getPrefixFun().get(), classFields)
                        : ClassMetadata.targetInstanceWithClassFields(classFields);
    }

}
