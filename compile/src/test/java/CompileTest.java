import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import yamert89.snoopy.compile.ClassMetadata;
import yamert89.snoopy.compile.ClassScanner;
import yamert89.snoopy.compile.MappedField;
import yamert89.snoopy.compile.adapters.ClassMetadataAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Order(1)
public class CompileTest {
    private String dataPath;

    @BeforeAll
    public static void init() {
        System.out.println("Compile tests start...");
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
        assertEquals(4, scanResult.size());
    }

    @Test
    public void correctMetadataForReplaceSqlAnnotation() throws IOException {
        testMetadata("ReplaceSQLExample.class", ClassMetadata.targetInstanceWithPrefix("SQL"));
    }

    @Test
    public void correctMetadataForReplaceSqlFieldAnnotation() throws IOException {
        Set<MappedField> mappedFields = Set.of(new MappedField("SQL2"));
        testMetadata("ReplaceSQLFieldExample.class", ClassMetadata.targetInstanceWithMappedFields(mappedFields));
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
