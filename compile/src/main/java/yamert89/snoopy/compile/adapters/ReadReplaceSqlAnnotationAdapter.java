package yamert89.snoopy.compile.adapters;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Type;
import yamert89.snoopy.compile.meta.Descriptors;
import yamert89.snoopy.meta.EmptyFilter;
import yamert89.snoopy.meta.Filter;

import java.util.function.Supplier;

public class ReadReplaceSqlAnnotationAdapter extends AnnotationVisitor {
    private Supplier<String> prefixFun;
    private Filter classFilter;

    public ReadReplaceSqlAnnotationAdapter(int api) {
        super(api);
        classFilter = new EmptyFilter();
    }

    @Override
    public void visit(String name, Object value) {
        switch (name) {
            case Descriptors.CLASS_FIELD_START_WITH -> {
                prefixFun = () -> (String) value;
            }
            case Descriptors.CLASS_FILTER -> {
                try {
                    classFilter = (Filter) getClass().getClassLoader().loadClass(((Type) value).getClassName())
                            .getConstructor().newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            default -> throw new RuntimeException("Unknown parameter in class annotation");
        }
        if (name.equals(Descriptors.CLASS_FIELD_START_WITH)) {
            prefixFun = () -> (String) value;
        }
        super.visit(name, value);
    }

    public Supplier<String> getPrefixFun() {
        return prefixFun;
    }

    public Filter getClassFilter() {
        return classFilter;
    }
}
