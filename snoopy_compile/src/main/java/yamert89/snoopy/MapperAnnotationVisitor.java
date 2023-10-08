package yamert89.snoopy;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Opcodes;

import java.lang.reflect.Field;

public class MapperAnnotationVisitor extends AnnotationVisitor {
    private static final String MAPPER_ANNOTATION_NAME = "Lyamert89/snoopy/SMapper;";
    protected MapperAnnotationVisitor(AnnotationVisitor annotationVisitor) {
        super(Opcodes.ASM9, annotationVisitor);
    }

    @Override
    public void visit(String name, Object value) {
        System.out.println("visit mapper annotation");
        if (value.toString().equals(MAPPER_ANNOTATION_NAME)){
            try {
                Class<?> mapperClass = Class.forName(MAPPER_ANNOTATION_NAME.replace("/", ".").substring(1, MAPPER_ANNOTATION_NAME.length() - 1));
                for (Field field : mapperClass.getDeclaredFields()) {
                    System.out.println("field name:" + field.getName()/* + ", field value: " + field.get()*/);
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

        }
        //super.visit(name, value);
    }
}
