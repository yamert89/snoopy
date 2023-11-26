package yamert89.snoopy.compile.visitors;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.TypePath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yamert89.snoopy.compile.ClassMetadata;
import yamert89.snoopy.compile.ResourcesUtil;

import java.io.*;
import java.net.URL;



public class TargetClassVisitor extends ClassVisitor {
    private final ClassVisitor cv;
    private final ClassMetadata classMetadata;
    private FieldsSet fieldsSet;
    private final Logger log = LoggerFactory.getLogger(TargetClassVisitor.class);

    public TargetClassVisitor(int api, ClassVisitor cv, ClassMetadata classMetadata) {
        super(api, cv);
        this.cv = cv;
        this.classMetadata = classMetadata;
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        log.debug("started analyzing field: {{}}", name);

        if (classMetadata.getTargetFieldsPrefix() != null && name.startsWith(classMetadata.getTargetFieldsPrefix())) {
            File resource = ResourcesUtil.getByName("/" + name + ".sql");
            if (resource != null ){
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(resource)));
                    StringBuilder strBuilder = new StringBuilder();
                    while (reader.ready()) {
                        strBuilder.append(reader.readLine());
                    }
                    String newValue = strBuilder.toString();
                    log.debug("Field's value \"{}\" was replaced with \"{}\"", value, newValue);
                    return new SQLFieldVisitor(cv.visitField(access, name, descriptor, signature, newValue));
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                    throw new RuntimeException(e);
                }
            }
            return super.visitField(access, name, descriptor, signature, value);

        }else return super.visitField(access, name, descriptor, signature, value);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        log.debug("started analyzing class annotation: {{}}", descriptor);
        return new ReplaceSQLAnnotationVisitor();
    }

    @Override
    public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        return super.visitTypeAnnotation(typeRef, typePath, descriptor, visible);
    }

    @Override
    public void visitSource(String source, String debug) {
        super.visitSource(source, debug);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        cv.visit(version, access, name, signature, superName, interfaces);
    }
}

enum FieldsSet {
    ALL,
    PREFIX,
    MAPPER
}
