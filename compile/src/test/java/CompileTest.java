import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import yamert89.snoopy.compile.ClassField;
import yamert89.snoopy.compile.ClassMetadata;
import yamert89.snoopy.compile.ClassScanner;
import yamert89.snoopy.compile.ResourcesUtil;
import yamert89.snoopy.compile.adapters.ClassMetadataAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Order(1)
public class CompileTest {
    private String dataPath;

    @BeforeAll
    public static void init() {
        System.out.println("Compile tests start...");
        ResourcesUtil.getInstance(new File("").getAbsolutePath() + "/build/resources/test");
    }
    @BeforeEach
    public void setProps(){
        String s = FileSystems.getDefault().getSeparator();
        String contextPath = new File("").getAbsolutePath();
        dataPath = contextPath + "/build/classes/java/test/data/".replace("/", s);
    }

    @Test
    public void classScannerWorks() throws IOException {

        ClassScanner scanner = new ClassScanner(dataPath);
        Set<String> scanResult = scanner.scan();
        String expected = dataPath + "ReplaceSQLExample.class";
        assertTrue(scanResult.contains(expected));
        assertEquals(6, scanResult.size());
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
                new ClassField("SQL2", true, true, "select * from sql2;"),
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
        ClassMetadataAdapter classMetadataAdapter = new ClassMetadataAdapter(Opcodes.ASM9);
        reader.accept(classMetadataAdapter, 0);
        assertEquals(exceptedMetadata, classMetadataAdapter.getClassMetadata());
        is.close();
    }

}
