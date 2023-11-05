package yamert89.snoopy.compile;

import java.io.IOException;
import java.util.Set;

public class DefaultClassModifier implements ClassModifier {
    @Override
    public void modify(String rootPath, String basePath) {
        try {
            Set<String> classes = new ClassScanner(rootPath, basePath).scan();
            classes.forEach(new ClassResolver()::resolve);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
