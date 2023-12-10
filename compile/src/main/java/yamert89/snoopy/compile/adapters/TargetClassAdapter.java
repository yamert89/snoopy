package yamert89.snoopy.compile.adapters;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yamert89.snoopy.compile.ClassMetadata;
import yamert89.snoopy.compile.MappedField;
import yamert89.snoopy.compile.ResourcesUtil;
import yamert89.snoopy.compile.meta.Descriptors;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static org.objectweb.asm.Opcodes.*;


public class TargetClassAdapter extends ClassVisitor {
    private final ClassMetadata classMetadata;
    private final Map<String, String> replacementFields = new HashMap<>();
    private final Logger log = LoggerFactory.getLogger(TargetClassAdapter.class);

    public TargetClassAdapter(int api, ClassVisitor cv, ClassMetadata classMetadata) {
        super(api, cv);
        this.classMetadata = classMetadata;
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
                    String newValue = strBuilder.toString();
                    log.debug("Field's value \"{}\" will be replace with \"{}\"", value, newValue);
                    replacementFields.put(name, newValue);
                    Object resultVal = access == ACC_PUBLIC + ACC_FINAL ? newValue : value;
                    return super.visitField(access, name, descriptor, signature, resultVal);
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                    throw new RuntimeException(e);
                }
            }
        }
        return super.visitField(access, name, descriptor, signature, value);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
        if (name.equals(Descriptors.INIT))
            return new InitMethodFieldsAssignAdapter(replacementFields, ASM9, mv);
        return mv;
    }

}
