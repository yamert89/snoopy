import data.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import service.ClearFolderVisitor;
import service.ReloadClassLoader;
import yamert89.snoopy.compile.ClassModifier;
import yamert89.snoopy.compile.DefaultClassModifier;
import yamert89.snoopy.compile.ResourcesUtil;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Order(3)
public class RuntimeTests {
    private static String buildPath;
    private static Object replaceSQLExample;
    private static Object replaceSQLFieldExample;
    private static Object notInitialized;
    private static Object getters;
    private static Object otherTypes;
    private static Object filters;

    @BeforeAll
    public static void modifyBytecode() throws Exception {
        System.out.println("Runtime tests start...");
        buildPath = System.getenv("snoopy.execPath");
        ResourcesUtil.getInstance(new File("").getAbsolutePath() + "/build/resources/test");
        ClassModifier classModifier = new DefaultClassModifier();
        classModifier.modify(buildPath + "/classes/java/test/data", buildPath + "/resources/test");
        var classLoader = new ReloadClassLoader();
        Class<?> cl = classLoader.loadClass(ReplaceSQLExample.class);
        replaceSQLExample = cl.getConstructor().newInstance();
        Class<?> clF = classLoader.loadClass(ReplaceSQLFieldExample.class);
        replaceSQLFieldExample = clF.getConstructor().newInstance();
        Class<?> clNI = classLoader.loadClass(NotInitialized.class);
        notInitialized = clNI.getConstructor().newInstance();
        Class<?> clGetters = classLoader.loadClass(Getters.class);
        getters = clGetters.getConstructor().newInstance();
        Class<?> clOtherTypes = classLoader.loadClass(OtherTypes.class);
        otherTypes = clOtherTypes.getConstructor().newInstance();
        Class<?> clFilters = classLoader.loadClass(Filters.class);
        filters = clFilters.getConstructor().newInstance();
    }

    //@AfterAll
    public static void clean() throws IOException {
        Files.walkFileTree(Paths.get(buildPath), new ClearFolderVisitor());
    }

    @Test
    public void finalField() throws Exception {
        String sql = getFieldValue("SQL1", replaceSQLExample);
        assertEquals(getSingleRowValue("SQL1"), sql);
    }

    @Test
    public void notFinalField() throws Exception {
        String sql = getFieldValue("SQL2", replaceSQLExample);
        assertEquals(getSingleRowValue("SQL2"), sql);
    }

    @Test
    public void privateNotFinalField() throws Exception {
        String sql = getFieldValue("SQL3", replaceSQLExample);
        assertEquals(getSingleRowValue("SQL3"), sql);
    }

    @Test
    public void privateNotFinalNotInitializedField() throws Exception {
        String sql = getFieldValue("SQL5", replaceSQLExample);
        assertEquals(getSingleRowValue("SQL5"), sql);
    }

    @Test
    public void fieldMarkedByReplaceSqlField() throws Exception {
        String sql = getFieldValue("SQL", replaceSQLFieldExample);
        assertEquals(getSingleRowValue("SQL2"), sql);
    }

    @Test
    public void notInitializedFieldInTheStart() throws Exception {
        String sql = getFieldValue("SQL1", notInitialized);
        assertEquals(getSingleRowValue("SQL1"), sql);
    }

    @Test
    public void notInitializedFieldInTheMiddle() throws Exception {
        String sql = getFieldValue("SQL3", notInitialized);
        assertEquals(getSingleRowValue("SQL3"), sql);
    }

    @Test
    public void notInitializedFieldInTheEnd() throws Exception {
        String sql = getFieldValue("SQL5", notInitialized);
        assertEquals(getSingleRowValue("SQL5"), sql);
    }

    @Test
    public void finalFieldGetter() throws Exception {
        String sql = getGetterValue("SQL1", getters);
        assertEquals(getSingleRowValue("SQL1"), sql);
    }

    @Test
    public void notFinalFieldGetter() throws Exception {
        String sql = getGetterValue("SQL2", getters);
        assertEquals(getSingleRowValue("SQL2"), sql);
    }

    @Test
    public void fieldAnnotationHasHighestPriority() throws Exception {
        String sql = getFieldValue("SQL1", otherTypes);
        assertEquals(getSingleRowValue("SQL2"), sql);
    }

    @Test
    public void filterOnFieldWorks() throws Exception {
        String sql = getFieldValue("SQL", filters);
        assertEquals("select col1 from table1 where col1 = ? and col2 = ?;", sql);
    }

    @Test
    public void filterOnClassWorks() throws Exception {
        String sql = getFieldValue("SQL2", filters);
        assertEquals("select * from sql2 where col1 = ? and col2 = 2;", sql);
    }

    private String getFieldValue(String fieldName, Object instance) throws NoSuchFieldException, IllegalAccessException {
        Field field = instance.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return (String) field.get(instance);
    }

    private String getGetterValue(String fieldName, Object instance) throws Exception {
        Method method = instance.getClass().getDeclaredMethod(
                "get"
                        + fieldName.substring(0, 1).toUpperCase()
                        + fieldName.substring(1));
        method.setAccessible(true);
        return (String) method.invoke(instance);
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
