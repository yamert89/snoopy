import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.ClassReader;
import yamert89.snoopy.compile.*;
import yamert89.snoopy.compile.adapters.ClassMetadataAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Order(1)
public class UnitTest {
    private String dataPath;
    private static String buildPath;

    @BeforeAll
    public static void init() throws IOException {
        System.out.println("Compile tests start...");
        String resourcesPath = new File("").getAbsolutePath() + "/build/resources/test";
        List<File> files = new FileScanner(resourcesPath, "sql").scan();
        ResourcesUtil.getInstance(files);
    }
    @BeforeEach
    public void setProps(){
        String s = FileSystems.getDefault().getSeparator();
        String contextPath = new File("").getAbsolutePath();
        buildPath = System.getenv("snoopy.execPath");
        dataPath = contextPath + "/build/classes/java/test/data/".replace("/", s);
    }

    @Test
    public void apiDoesNotThrowException() {
        assertDoesNotThrow(() -> {
            ClassModifier classModifier = new DefaultClassModifier();
            classModifier.modify(Collections.emptyList(), Collections.emptyList());
        });
    }

    @Test
    public void classScannerWorks() throws IOException {

        FileScanner scanner = new FileScanner(dataPath, "class");
        List<String> scanResult = scanner.scan().stream().map(File::getAbsolutePath).toList();
        String expected = dataPath + "ReplaceSQLExample.class";
        assertTrue(scanResult.contains(expected));
        assertEquals(8, scanResult.size());
    }

    @Test
    public void correctMetadataForReplaceSqlAnnotation() throws IOException {
        ClassMetadata expected = new ClassMetadata("SQL", List.of(
                new ClassField("SQL1", true, true, "select * from sql1;"),
                new ClassField("SQL2", true, true, "select * from sql2;"),
                new ClassField("SQL3", true, true, "select * from sql3;"),
                ClassField.notTargetInstance("regularField"),
                new ClassField("SQL4", true, true, "select * from sql4;"),
                new ClassField("SQL5", true, false, "select * from sql5;")
        ));

        testMetadata("ReplaceSQLExample.class", expected);
    }

    @Test
    public void correctMetadataForReplaceSqlFieldAnnotation() throws IOException {
        ClassMetadata expected = ClassMetadata.targetInstanceWithClassFields(List.of(
                new ClassField("SQL", true, true, "select * from sql2;"),
                ClassField.notTargetInstance("regularField")
        ));
        testMetadata("ReplaceSQLFieldExample.class", expected);
    }

    @Test
    public void correctMetadataForRegularClass() throws IOException {
        testMetadata("RegularClass.class", ClassMetadata.notTargetInstance());
    }

    private void testMetadata(String className, ClassMetadata exceptedMetadata) throws IOException{
        var is = new FileInputStream(dataPath + className);
        var reader = new ClassReader(is);
        ClassMetadataAdapter classMetadataAdapter = new ClassMetadataAdapter();
        reader.accept(classMetadataAdapter, 0);
        assertEquals(exceptedMetadata, classMetadataAdapter.getClassMetadata());
        is.close();
    }

}
