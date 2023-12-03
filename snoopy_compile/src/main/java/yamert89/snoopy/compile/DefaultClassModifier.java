package yamert89.snoopy.compile;

import java.io.IOException;
import java.util.Set;

public class DefaultClassModifier implements ClassModifier {
    @Override
    public void modify(String classDir, String resourcesDir) {
        try {
            ResourcesUtil.getInstance(resourcesDir);
            Set<String> classes = new ClassScanner(classDir).scan();
            classes.forEach((file) -> new ClassResolver(file).resolve());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
