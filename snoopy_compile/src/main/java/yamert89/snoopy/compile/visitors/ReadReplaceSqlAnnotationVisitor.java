package yamert89.snoopy.compile.visitors;

import org.objectweb.asm.AnnotationVisitor;

import java.util.function.Supplier;

public class ReadReplaceSqlAnnotationVisitor extends AnnotationVisitor {
    private Supplier<String> prefixFun;
    public ReadReplaceSqlAnnotationVisitor(int api) {
        super(api);
    }

    @Override
    public void visit(String name, Object value) {
        prefixFun = () -> (String) value;
        super.visit(name, value);
    }

    public Supplier<String> getPrefixFun() {
        return prefixFun;
    }
}
