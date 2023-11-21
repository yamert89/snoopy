package yamert89.snoopy.compile;

import org.objectweb.asm.*;

import java.io.*;
import java.net.URL;

public class InjectFieldVisitor extends ClassVisitor {
    private final ClassVisitor cv;
    private final ClassMetadata classMetadata;
    private FieldsSet fieldsSet;

    public InjectFieldVisitor(int api, ClassVisitor cv, ClassMetadata classMetadata) {
        super(api, cv);
        this.cv = cv;
        this.classMetadata = classMetadata;
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        System.out.println("visit field: " + name);

        if (classMetadata.getTargetFieldsPrefix() != null && name.startsWith(classMetadata.getTargetFieldsPrefix())) {
            URL url = this.getClass().getResource("/" + name + ".sql");
            if (url != null ){
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(url.getFile())));
                    StringBuilder strBuilder = new StringBuilder();
                    while (reader.ready()) {
                        strBuilder.append(reader.readLine());
                    }
                    String fieldValue = strBuilder.toString();
                    System.out.println("new value of field " + name + " = " + fieldValue);
                    return new SQLFiledVisitor(cv.visitField(access, name, descriptor, signature, fieldValue));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return super.visitField(access, name, descriptor, signature, value);

        }else return super.visitField(access, name, descriptor, signature, value);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        System.out.println("visit annotation: " + descriptor);
        return new ReplaceSQLAnnotationVisitor();
    }

    @Override
    public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        System.out.printf("visit type annotation %n, %s, %s" + typeRef, typePath, descriptor);
        return super.visitTypeAnnotation(typeRef, typePath, descriptor, visible);
    }

    @Override
    public void visitSource(String source, String debug) {
        System.out.println("visit source " + source);
        super.visitSource(source, debug);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        System.out.println("visit: " + name);
        cv.visit(version, access, name, signature, superName, interfaces);
    }
}

enum FieldsSet {
    ALL,
    PREFIX,
    MAPPER
}
