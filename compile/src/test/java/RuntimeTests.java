import data.ReplaceSQLExample;
import data.ReplaceSQLFieldExample;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import yamert89.snoopy.compile.ClassModifier;
import yamert89.snoopy.compile.DefaultClassModifier;

import java.io.IOException;
import java.lang.reflect.Field;
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
        //Files.walkFileTree(Paths.get(buildPath), new ClearFolderVisitor());
    }

    @Test
    public void finalField() throws Exception {
        String sql1 = getField("SQL1", new ReplaceSQLExample());
        assertEquals(getSingleRowValue("SQL1"), sql1);
    }

    @Test
    public void notFinalField() throws Exception {
        String sql2 = getField("SQL2", new ReplaceSQLExample());
        assertEquals(getSingleRowValue("SQL2"), sql2);
    }

    @Test
    public void privateNotFinalField() throws Exception {
        String sql3 = getField("SQL3", new ReplaceSQLExample());
        assertEquals(getSingleRowValue("SQL3"), sql3);
    }

    @Test
    public void fieldMarkedByReplaceSqlField() throws Exception {
        String sql2 = getField("SQL2", new ReplaceSQLFieldExample());
        assertEquals(getSingleRowValue("SQL2"), sql2);
    }



    private String getField(String fieldName, Object instance) throws NoSuchFieldException, IllegalAccessException {
        Field field = instance.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return (String) field.get(instance);
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
