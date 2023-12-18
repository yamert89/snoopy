package yamert89.snoopy.compile.adapters;

import org.objectweb.asm.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yamert89.snoopy.compile.ClassField;
import yamert89.snoopy.compile.ClassMetadata;
import yamert89.snoopy.compile.ResourcesUtil;
import yamert89.snoopy.compile.meta.Descriptors;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

import static org.objectweb.asm.Opcodes.ASM9;
import static org.objectweb.asm.Opcodes.PUTFIELD;

public class ClassMetadataAdapter extends ClassVisitor {
    private final List<ClassField> classFields;

    private final ReadReplaceSqlAnnotationAdapter annotationVisitor = new ReadReplaceSqlAnnotationAdapter(Opcodes.ASM9);

    private final Logger log = LoggerFactory.getLogger(ClassMetadataAdapter.class);


    public ClassMetadataAdapter(int api) {
        super(api);
        classFields = new LinkedList<>();
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
        if (annotationVisitor.getPrefixFun() != null) {
            String targetFieldPrefix = annotationVisitor.getPrefixFun().get();
            if (descriptor.equals(Descriptors.STRING) && name.startsWith(targetFieldPrefix))
                classFields.add(getTargetClassField(name, (String) value));
            else classFields.add(ClassField.notTargetInstance(name));
            return super.visitField(access, name, descriptor, signature, value);
        }
        return new SingleFieldAdapter(
                ASM9,
                () -> classFields.add(getTargetClassField(name, (String) value)),
                () -> classFields.add(ClassField.notTargetInstance(name))
        );
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        if (name.equals(Descriptors.INIT) && !classFields.isEmpty() && classFields.stream().anyMatch(ClassField::isInitialized)) {
            return new MethodVisitor(ASM9) {
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

    private ClassField getTargetClassField(String name, String value) {
        File resource = ResourcesUtil.getByName("/" + name + ".sql");
        if (resource != null) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(resource)));
                StringBuilder strBuilder = new StringBuilder();
                while (reader.ready()) {
                    strBuilder.append(reader.readLine());
                }
                reader.close();
                String newValue = strBuilder.toString();
                log.debug("Field's value \"{}\" will be replace with \"{}\"", value, newValue);

                return new ClassField(name, true, value != null, newValue);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                throw new RuntimeException(e);
            }
        }
        throw new RuntimeException(String.format("Resource /%s.sql not found", name));
    }
}
