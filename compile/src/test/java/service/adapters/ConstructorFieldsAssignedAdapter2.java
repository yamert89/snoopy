package service.adapters;

import com.github.yamert89.snoopy.compile.meta.Descriptors;
import com.github.yamert89.snoopy.meta.InjectSQLField;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;

import java.util.ArrayList;
import java.util.List;

import static com.github.yamert89.snoopy.compile.Constants.API_VERSION;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Adapter for fields marked as {@link InjectSQLField}
 * */
public class ConstructorFieldsAssignedAdapter2 extends ClassVisitor {
    private final String expectedValue;

    public ConstructorFieldsAssignedAdapter2(String expectedValue) {
        super(API_VERSION);
        this.expectedValue = expectedValue;
    }

    private final List<String> fieldNames = new ArrayList<>(1);
    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        if (fieldNames.contains(name)){
            assertEquals(expectedValue, value);
        }
        return new FieldVisitor(API_VERSION) {
            @Override
            public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
                assertEquals(Descriptors.REPLACE_SQL_FIELD, descriptor);
                if (Descriptors.REPLACE_SQL_FIELD.equals(descriptor)) fieldNames.add(name);
                return super.visitAnnotation(descriptor, visible);
            }
        };
    }
}
