package fakes;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;
import yamert89.snoopy.compile.meta.Descriptors;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Adapter for fields marked as {@link yamert89.snoopy.compile.meta.ReplaceSQLField}
 * */
public class ConstructorFieldsAssignedAdapter2 extends ClassVisitor {
    private final String expectedValue;

    public ConstructorFieldsAssignedAdapter2(int api, String expectedValue) {
        super(api);
        this.expectedValue = expectedValue;
    }

    private final List<String> fieldNames = new ArrayList<>(1);
    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        if (fieldNames.contains(name)){
            assertEquals(expectedValue, value);
        }
        return new FieldVisitor(Opcodes.ASM9) {
            @Override
            public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
                assertEquals(Descriptors.REPLACE_SQL_FIELD, descriptor);
                if (Descriptors.REPLACE_SQL_FIELD.equals(descriptor)) fieldNames.add(name);
                return super.visitAnnotation(descriptor, visible);
            }
        };
    }
}
