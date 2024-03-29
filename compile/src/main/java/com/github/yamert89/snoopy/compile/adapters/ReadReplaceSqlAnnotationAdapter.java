package com.github.yamert89.snoopy.compile.adapters;

import com.github.yamert89.snoopy.compile.meta.Descriptors;
import com.github.yamert89.snoopy.meta.EmptyFilter;
import com.github.yamert89.snoopy.meta.Filter;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Type;

import java.util.function.Supplier;

import static com.github.yamert89.snoopy.compile.Constants.API_VERSION;

public class ReadReplaceSqlAnnotationAdapter extends AnnotationVisitor {
    private Supplier<String> prefixFun;
    private Filter classFilter;

    public ReadReplaceSqlAnnotationAdapter() {
        super(API_VERSION);
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
