package yamert89.snoopy.compile.adapters;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yamert89.snoopy.compile.ClassField;
import yamert89.snoopy.compile.ClassMetadata;
import yamert89.snoopy.compile.MappedField;
import yamert89.snoopy.compile.ResourcesUtil;
import yamert89.snoopy.compile.meta.Descriptors;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.objectweb.asm.Opcodes.*;


public class TargetClassAdapter extends ClassVisitor {
    private final ClassMetadata classMetadata;
    private final List<ClassField> classFields;
    private String className;
    private final Logger log = LoggerFactory.getLogger(TargetClassAdapter.class);

    public TargetClassAdapter(int api, ClassVisitor cv, ClassMetadata classMetadata) {
        super(api, cv);
        this.classMetadata = classMetadata;
        classFields = new ArrayList<>();
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        className = name;
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        log.debug("started analyzing field: {{}}", name);

        if (
                classMetadata.getTargetFieldsPrefix() != null && name.startsWith(classMetadata.getTargetFieldsPrefix())
                        || classMetadata.getMappedFields().contains(new MappedField(name))
        ) {
            File resource = ResourcesUtil.getByName("/" + name + ".sql");
            if (resource != null ){
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(resource)));
                    StringBuilder strBuilder = new StringBuilder();
                    while (reader.ready()) {
                        strBuilder.append(reader.readLine());
                    }
                    reader.close();
                    String newValue = strBuilder.toString();
                    log.debug("Field's value \"{}\" will be replace with \"{}\"", value, newValue);

                    classFields.add(new ClassField(name, true, value != null, newValue));

                    Object resultVal = access == ACC_PUBLIC + ACC_FINAL ? newValue : value;
                    return super.visitField(access, name, descriptor, signature, resultVal);
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                    throw new RuntimeException(e);
                }
            }
        } else classFields.add(ClassField.notTargetInstance(name));
        return super.visitField(access, name, descriptor, signature, value);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
        if (name.equals(Descriptors.INIT) && !classFields.isEmpty() && classFields.stream().anyMatch(ClassField::isTarget))
            return new InitMethodFieldsAssignAdapter(new LinkedList<>(classFields), className, ASM9, mv);
        return mv;
    }

}
