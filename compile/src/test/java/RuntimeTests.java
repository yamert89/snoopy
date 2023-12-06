import data.ReplaceSQLExample;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import service.ClearFolderVisitor;
import yamert89.snoopy.compile.ClassModifier;
import yamert89.snoopy.compile.DefaultClassModifier;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RuntimeTests {
    private static String buildPath;

    @BeforeAll
    public static void modifyBytecode() {
        buildPath = System.getenv("snoopy.execPath");
        ClassModifier classModifier = new DefaultClassModifier();
        classModifier.modify(buildPath + "/classes/java/test/data", buildPath + "/resources/test");
    }

    @AfterAll
    public static void clean() throws IOException {
        Files.walkFileTree(Paths.get(buildPath), new ClearFolderVisitor());
    }

    @Test
    public void finalField() throws Exception {
        String sql1 = getField("SQL1", new ReplaceSQLExample());
        assertEquals(getSingleRowValue("SQL1"), sql1);
    }

    private String getField(String fieldName, Object instance) throws NoSuchFieldException, IllegalAccessException {
        return (String) instance.getClass().getField(fieldName).get(instance);
    }

    private String getSingleRowValue(String fileName) {
        return new String(readResource(fileName + ".sql"), StandardCharsets.UTF_8);
    }

    private byte[] readResource(String name) {
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
