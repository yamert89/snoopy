package com.github.yamert89.snoopy.compile.adapters;

import com.github.yamert89.snoopy.compile.ClassField;
import com.github.yamert89.snoopy.compile.Constants;
import com.github.yamert89.snoopy.compile.ResourcesUtil;
import com.github.yamert89.snoopy.compile.meta.Descriptors;
import com.github.yamert89.snoopy.meta.Filter;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.function.Consumer;

public class SingleFieldAdapter extends FieldVisitor {
    private boolean fieldIsTarget;
    private final Object oldValue;
    private final String fieldName;
    private final boolean isTargetByClassLevel;
    private final Consumer<ClassField> addFunc;
    private String resourceName;
    private Filter filter;
    private final Logger log = LoggerFactory.getLogger(SingleFieldAdapter.class);

    public SingleFieldAdapter(Object oldValue, String fieldName, boolean isTargetByClassLevel, Consumer<ClassField> addFunc, Filter filter) {
        super(Constants.API_VERSION);
        this.oldValue = oldValue;
        this.fieldName = fieldName;
        this.isTargetByClassLevel = isTargetByClassLevel;
        this.addFunc = addFunc;
        fieldIsTarget = false;
        this.filter = filter;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        if (descriptor.equals(Descriptors.REPLACE_SQL_FIELD)) {
            fieldIsTarget = true;
            return new AnnotationVisitor(Constants.API_VERSION) {
                @Override
                public void visit(String name, Object value) {
                    switch (name) {
                        case Descriptors.FIELD_RESOURCE_NAME:
                            resourceName = (String) value;
                            break;
                        case Descriptors.FIELD_FILTER:
                            try {
                                String filterClassName = ((Type) value).getClassName();
                                filter = (Filter) getClass().getClassLoader().loadClass(filterClassName)
                                        .getConstructor().newInstance();
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                            break;
                        default:
                            throw new RuntimeException("Unknown annotation parameter");
                    }
                    super.visit(name, value);
                }
            };
        }
        return super.visitAnnotation(descriptor, visible);
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
        if (resourceName == null) resourceName = fieldName;
        if (fieldIsTarget) addFunc.accept(getTargetClassField(resourceName));
        if (!fieldIsTarget) {
            ClassField cf = isTargetByClassLevel ?
                    getTargetClassField(fieldName)
                    :
                    ClassField.notTargetInstance(fieldName);
            addFunc.accept(cf);
        }
    }

    private ClassField getTargetClassField(String resourceName) {
        File resource = ResourcesUtil.getInstance().getByName(resourceName + ".sql");
        if (resource != null) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(resource)));
                StringBuilder strBuilder = new StringBuilder();
                while (reader.ready()) {
                    strBuilder.append(reader.readLine());
                }
                reader.close();
                String newValue = filter.apply(strBuilder.toString());
                log.debug("Field's value \"{}\" will be replace with \"{}\"", oldValue, newValue);

                return new ClassField(fieldName, true, oldValue != null, newValue);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                throw new RuntimeException(e);
            }
        }
        throw new RuntimeException(String.format("Resource /%s.sql not found", resourceName));
    }
}
