package yamert89.snoopy.compile;

import java.io.File;
import java.util.List;

public class DefaultClassModifier implements ClassModifier {

    @Override
    public void modify(List<File> classes, List<File> resources) {
        ResourcesUtil.getInstance(resources);
        classes.forEach((file) -> new ClassResolver(file).resolve());
    }
}
