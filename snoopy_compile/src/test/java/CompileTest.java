import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.ClassReader;
import yamert89.snoopy.compile.ClassScanner;
import yamert89.snoopy.compile.InjectSqlClassAcceptor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        String expected = dataPath + "Simple.class";
        assertTrue(scanResult.contains(expected));
        assertEquals(2, scanResult.size());
    }

    @Test
    public void injectSqlClassAcceptorWorks() throws IOException {
        var is = new FileInputStream(dataPath + "Simple.class");
        assertTrue(new InjectSqlClassAcceptor().accepted(new ClassReader(is)));
    }


}
