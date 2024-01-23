import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.util.CheckClassAdapter;
import service.adapters.ConstructorFieldsAssignedAdapter;
import service.adapters.FieldsCounterAdapter;
import service.adapters.InitCounterAdapter;
import yamert89.snoopy.compile.ClassField;
import yamert89.snoopy.compile.ClassMetadata;
import yamert89.snoopy.compile.ResourcesUtil;
import yamert89.snoopy.compile.adapters.TargetClassAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Order(2)
public class ByteCodeTest {

    private static ClassMetadata replaceSQLMetadata;
    private static ClassMetadata gettersMetadata;
    private static final String REPLACE_SQL_EXAMPLE_CLASS_NAME = "ReplaceSQLExample.class";
    private static final String GETTERS_CLASS_NAME = "Getters.class";
    private static String dataPath;

    @BeforeAll
    public static void init() throws IOException {
        System.out.println("Bytecode tests start...");
        String resourcesPath = new File("").getAbsolutePath() + "/build/resources/test";
        List<File> files = Files.walk(Paths.get(resourcesPath)).map(Path::toFile).collect(Collectors.toList());
        ResourcesUtil.getInstance(files);
        String s = FileSystems.getDefault().getSeparator();
        String contextPath = new File("").getAbsolutePath();
        dataPath = contextPath + "/build/classes/java/test/data/".replace("/", s);
    }

    @BeforeEach
    public void assign() {
        replaceSQLMetadata = new ClassMetadata("SQL", new LinkedList<>(List.of(
                new ClassField("SQL1", true, true, "select * from sql1;"),
                new ClassField("SQL2", true, true, "select * from sql2 where col1 = 1 and col2 = 2;"),
                new ClassField("SQL3", true, true, "select * from sql3;"),
                ClassField.notTargetInstance("regularField"),
                new ClassField("SQL4", true, true, "select * from sql4;"),
                new ClassField("SQL5", true, false, "select * from sql5;")
        )));
        gettersMetadata = ClassMetadata.targetInstanceWithClassFields(new LinkedList<>(List.of(
                ClassField.notTargetInstance("regularField"),
                new ClassField("SQL1", true, true, "select * from sql1;"),
                new ClassField("SQL2", true, true, "select * from sql2;")
        )));
    }

    @Test
    public void finalField() throws IOException {
        testFieldsInTheSameClass(
                REPLACE_SQL_EXAMPLE_CLASS_NAME,
                new ConstructorFieldsAssignedAdapter(Opcodes.ASM9, "SQL1", new String(readResource("SQL1.sql"), StandardCharsets.UTF_8)),
                replaceSQLMetadata
        );
    }

    @Test
    public void notFinalField() throws IOException {
        System.out.println("notFinal: " + System.nanoTime());
        testFieldsInTheSameClass(
                REPLACE_SQL_EXAMPLE_CLASS_NAME,
                new ConstructorFieldsAssignedAdapter(Opcodes.ASM9, "SQL2", new String(readResource("SQL2.sql"), StandardCharsets.UTF_8)),
                replaceSQLMetadata
        );
    }

    @Test
    public void privateNotFinalField() throws IOException {
        testFieldsInTheSameClass(
                REPLACE_SQL_EXAMPLE_CLASS_NAME,
                new ConstructorFieldsAssignedAdapter(Opcodes.ASM9, "SQL3", new String(readResource("SQL3.sql"), StandardCharsets.UTF_8)),
                replaceSQLMetadata
        );
    }

    @Test
    public void calculateFieldsInReplaceSQLExample() throws Exception {
        testFieldsInTheSameClass(
                REPLACE_SQL_EXAMPLE_CLASS_NAME,
                new FieldsCounterAdapter(6),
                replaceSQLMetadata
        );
    }

    @Test
    public void calculateFieldsInGetters() throws Exception {
        testFieldsInTheSameClass(
                GETTERS_CLASS_NAME,
                new FieldsCounterAdapter(3),
                replaceSQLMetadata
        );
    }

    @Test
    public void calculateInitAssignablesInReplaceSQLExample() throws Exception {
        testFieldsInTheSameClass(
                REPLACE_SQL_EXAMPLE_CLASS_NAME,
                new InitCounterAdapter(6),
                replaceSQLMetadata
        );
    }

    @Test
    public void calculateInitAssignablesInGetters() throws Exception {
        testFieldsInTheSameClass(
                GETTERS_CLASS_NAME,
                new InitCounterAdapter(2),
                gettersMetadata
        );
    }

    /*@Test
    public void fieldMarkedByReplaceSqlField() throws IOException {
        List<ClassField> classFields = List.of(
                new ClassField("SQL2", true, true, "select * from sql2;"),
                ClassField.notTargetInstance("regularField")
        );
        testFieldsInTheSameClass(
                "ReplaceSQLFieldExample.class",
                new ConstructorFieldsAssignedAdapter2(Opcodes.ASM9, new String(readResource("SQL2.sql"), StandardCharsets.UTF_8)),
                ClassMetadata.targetInstanceWithClassFields(classFields)
        );
    }*/

    private void testFieldsInTheSameClass(String className, ClassVisitor cv, ClassMetadata clMetadata) throws IOException {
        File targetFile = createTargetFile(className, clMetadata);
        try (var is = new FileInputStream(targetFile);) {
            var reader = new ClassReader(is);
            reader.accept(cv, 0);
        }
    }

    private File createTargetFile(String fileName, ClassMetadata clMetadata) throws IOException {
        var classFilePath = getV0(fileName);
        var targetStringPath = getV1(fileName);
        var is = new FileInputStream(classFilePath);
        var reader = new ClassReader(is);
        var writer = new ClassWriter(reader, 0);
        TargetClassAdapter targetClassAdapter = new TargetClassAdapter(Opcodes.ASM9, new CheckClassAdapter(writer), clMetadata);
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
        return dataPath + fileName;
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
