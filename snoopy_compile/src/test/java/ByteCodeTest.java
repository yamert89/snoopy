import fakes.ConstructorFieldsAssignedAdapter;
import fakes.ConstructorFieldsAssignedAdapter2;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import yamert89.snoopy.compile.ClassMetadata;
import yamert89.snoopy.compile.ResourcesUtil;
import yamert89.snoopy.compile.adapters.TargetClassAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class ByteCodeTest {

    @BeforeAll
    public static void init(){
        ResourcesUtil.getInstance(new File("").getAbsolutePath() + "/build/resources/test");
    }

    @Test
    public void finalFieldInjectedSuccessfully() throws IOException {
        testClass(
                "ReplaceSQLExampleCL.class",
                new ConstructorFieldsAssignedAdapter(Opcodes.ASM9, "SQL1", new String(readResource("SQL1.sql"), StandardCharsets.UTF_8)),
                new ClassMetadata(true, "SQL")
        );
    }

    @Test
    public void notFinalFieldInjectedSuccessfully() throws IOException {
        testClass(
                "ReplaceSQLExampleCL.class",
                new ConstructorFieldsAssignedAdapter(Opcodes.ASM9, "SQL2", new String(readResource("SQL2.sql"), StandardCharsets.UTF_8)),
                new ClassMetadata(true, "SQL")
        );
    }

    @Test
    public void privateNotFinalFieldInjectedSuccessfully() throws IOException {
        testClass(
                "ReplaceSQLExampleCL.class",
                new ConstructorFieldsAssignedAdapter(Opcodes.ASM9, "SQL3", new String(readResource("SQL3.sql"), StandardCharsets.UTF_8)),
                new ClassMetadata(true, "SQL")
        );
    }

    @Test
    public void fieldMarkedByReplaceSqlFieldInjectedSuccessfully() throws IOException {
        testClass(
                "ReplaceSQLFieldExampleCL.class",
                new ConstructorFieldsAssignedAdapter2(Opcodes.ASM9, new String(readResource("SQL2.sql"), StandardCharsets.UTF_8)),
                new ClassMetadata(true, null)
        );
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
        TargetClassAdapter targetClassAdapter = new TargetClassAdapter(Opcodes.ASM9, writer, clMetadata);
        reader.accept(targetClassAdapter, 0);
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
