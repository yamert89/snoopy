import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.ClassReader;
import yamert89.snoopy.compile.ClassScanner;
import yamert89.snoopy.compile.InjectSqlClassAcceptor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class CompileTest {
    private String dataPath;
    @BeforeEach
    public void setProps(){
        String s = FileSystems.getDefault().getSeparator();
        String contextPath = new File("").getAbsolutePath();
        dataPath = contextPath + "/build/classes/java/test/data/".replace("/", s);
    }

    @Test
    public void classScannerWorks() throws IOException {

        ClassScanner scanner = new ClassScanner(dataPath, "");
        Set<String> scanResult = scanner.scan();
        String expected = dataPath + "ReplaceSQLExample.class";
        assertTrue(scanResult.contains(expected));
        assertEquals(3, scanResult.size());
    }

    @Test
    public void injectSqlClassAcceptorFoundReplaceSqlAnnotation() throws IOException {
        var is = new FileInputStream(dataPath + "ReplaceSQLExample.class");
        assertTrue(new InjectSqlClassAcceptor().accepted(new ClassReader(is)));
    }

    @Test
    public void injectSqlClassAcceptorFoundMapperAnnotation() throws IOException {
        var is = new FileInputStream(dataPath + "ReplaceSQLFieldExample.class");
        assertTrue(new InjectSqlClassAcceptor().accepted(new ClassReader(is)));
    }

    @Test
    public void injectSqlClassAcceptorNotFoundRegularClass() throws IOException {
        var is = new FileInputStream(dataPath + "RegularClass.class");
        assertFalse(new InjectSqlClassAcceptor().accepted(new ClassReader(is)));
    }

}
