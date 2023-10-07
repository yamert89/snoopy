package yamert89.snoopy;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes.*;

import static org.objectweb.asm.Opcodes.*;

public class InjectFieldVisitor extends ClassVisitor {
    private final ClassVisitor cv;

    public InjectFieldVisitor(int api, ClassVisitor cv) {
        super(api, cv);
        this.cv = cv;
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        if (name.startsWith("SQL")) {
            System.out.printf("%s before:%s", name, value);
            return cv.visitField(access, name, descriptor, signature, "select 1 from dual");
        }else return super.visitField(access, name, descriptor, signature, value);
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
