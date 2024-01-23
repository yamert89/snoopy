package yamert89.snoopy.compile;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class ResourcesUtil {
    private final List<File> resources;
    private static ResourcesUtil instance;

    private ResourcesUtil(List<File> resources) {
        this.resources = resources;
    }

    public static ResourcesUtil getInstance(List<File> resources) {
        if (instance == null /*|| !instance.resourcesDir.equals(resourcesDir)*/) {
            instance = new ResourcesUtil(Collections.unmodifiableList(resources));
        }
        return instance;
    }

    public static ResourcesUtil getInstance() {
        if (instance == null) throw new RuntimeException("ResourcesUtil not initialized");
        return instance;
    }

    public File getByName(String name) {
        return resources.stream().filter(f -> f.getName().equals(name)).findFirst().get();
    }
}
