package yamert89.snoopy.compile.adapters;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;
import yamert89.snoopy.compile.ClassMetadata;
import yamert89.snoopy.compile.MappedField;
import yamert89.snoopy.compile.meta.Descriptors;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class ClassMetadataAdapter extends ClassVisitor {
    private boolean isTarget = false;
    private final Set<MappedField> mappedFields = new HashSet<>();

    private final ReadReplaceSqlAnnotationAdapter annotationVisitor = new ReadReplaceSqlAnnotationAdapter(Opcodes.ASM9);

    public ClassMetadataAdapter(int api) {
        super(api);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        if (descriptor.equals(Descriptors.REPLACE_SQL)) {
            setAsTarget();
            return annotationVisitor;
        }
        return super.visitAnnotation(descriptor, visible);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        return new FieldVisitor(Opcodes.ASM9) {
            @Override
            public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
                if (descriptor.equals(Descriptors.REPLACE_SQL_FIELD)) {
                    setAsTarget();
                    mappedFields.add(new MappedField(name));
                }
                return super.visitAnnotation(descriptor, visible);
            }
        };
    }

    private void setAsTarget(){
        isTarget = true;
        visitEnd();
    }

    public ClassMetadata getClassMetadata() {
        Supplier<String> targetFieldsPrefixFun = annotationVisitor.getPrefixFun();
        return targetFieldsPrefixFun == null && mappedFields.isEmpty() ?
                ClassMetadata.notTargetInstance()
                :
                mappedFields.isEmpty() ?
                        ClassMetadata.targetInstanceWithPrefix(targetFieldsPrefixFun.get())
                        :
                        ClassMetadata.targetInstanceWithMappedFields(mappedFields);
    }
}
