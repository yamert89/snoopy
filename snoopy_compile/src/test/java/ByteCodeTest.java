import org.junit.jupiter.api.Test;
import org.objectweb.asm.*;
import yamert89.snoopy.compile.ClassMetadata;
import yamert89.snoopy.compile.InjectFieldVisitor;
import yamert89.snoopy.compile.meta.Descriptors;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ByteCodeTest {

    @Test
    public void fieldSQL1InTargetReplaceSQLExampleCLConvertedSuccessfully() throws IOException {
        testClass("ReplaceSQLExampleCL.class", new ClassVisitor(Opcodes.ASM9) {
            @Override
            public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
                if (name.equals("SQL1")) {
                    assertEquals(new String(readResource("SQL1.sql"), StandardCharsets.UTF_8), value);
                }
                return super.visitField(access, name, descriptor, signature, value);
            }
        }, new ClassMetadata(true, "SQL"));
    }

    @Test
    public void fieldSQL2InTargetReplaceSQLFieldExampleCLConvertedSuccessfully() throws IOException {
        testClass("ReplaceSQLFieldExampleCL.class", new ClassVisitor(Opcodes.ASM9) {
            private final List<String> fieldNames = new ArrayList<>(1);
            @Override
            public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
                if (fieldNames.contains(name)){
                    assertEquals(new String(readResource("SQL2.sql"), StandardCharsets.UTF_8), value);
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
        }, new ClassMetadata(true, null));
    }

    @Test
    public void fieldInject2InTargetReplaceSQLExample2CLConvertedSuccessfully() throws IOException {
        testClass("ReplaceSQLExample2CL.class", new ClassVisitor(Opcodes.ASM9) {
            @Override
            public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
                if (name.equals("inject2")) {
                    assertEquals(new String(readResource("inject2.sql"), StandardCharsets.UTF_8), value);
                }
                return super.visitField(access, name, descriptor, signature, value);
            }
        }, new ClassMetadata(true, "inject2"));
    }

    private void testClass(String className, ClassVisitor cv, ClassMetadata clMetadata) throws IOException{
        File targetFile = createTargetFile(className, clMetadata);
        var is = new FileInputStream(targetFile);
        var reader = new ClassReader(is);
        reader.accept(cv, 0);
        is.close();
    }

    private File createTargetFile(String fileName, ClassMetadata clMetadata) throws IOException {
        var classFilePath = getV0(fileName);
        var targetStringPath = getV1(fileName);
        var is = new FileInputStream(classFilePath);
        var reader = new ClassReader(is);
        var writer = new ClassWriter(reader, 0);
        InjectFieldVisitor injectFieldVisitor = new InjectFieldVisitor(Opcodes.ASM9, writer, clMetadata);
        reader.accept(injectFieldVisitor, 0);
        var bytes = writer.toByteArray();
        var targetFile = new File(targetStringPath);
        is.close();
        var targetPath = targetFile.toPath();
        Files.deleteIfExists(targetPath);
        Files.createFile(targetPath);
        var out = new FileOutputStream(targetFile);
        out.write(bytes);
        out.close();
        return targetFile;
    }

    private String getV0(String fileName){
        return Objects.requireNonNull(this.getClass().getResource("bytecode/v0")).getPath() + "/" + fileName;
    }

    private String getV1(String fileName){
        return Objects.requireNonNull(this.getClass().getResource("bytecode/v1")).getPath() + "/" + fileName;
    }

    private byte[] readResource(String name){
        try {
            return Files.readAllBytes(
                    Paths.get(Objects.requireNonNull(
                                    this.getClass().getResource(name))
                            .getFile()
                            .substring(1)
                    )
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



}
