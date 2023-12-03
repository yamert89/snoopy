package yamert89.snoopy.compile.adapters;

import org.objectweb.asm.AnnotationVisitor;

import java.util.function.Supplier;

public class ReadReplaceSqlAnnotationAdapter extends AnnotationVisitor {
    private Supplier<String> prefixFun;
    public ReadReplaceSqlAnnotationAdapter(int api) {
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
