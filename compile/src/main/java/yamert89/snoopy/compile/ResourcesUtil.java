package yamert89.snoopy.compile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ResourcesUtil {
    private final String resourcesDir;
    private static ResourcesUtil instance;

    private ResourcesUtil(String resourcesDir) {
        this.resourcesDir = resourcesDir;
    }

    public static ResourcesUtil getInstance(String resourcesDir) {
        if (instance == null /*|| !instance.resourcesDir.equals(resourcesDir)*/) {
            instance = new ResourcesUtil(resourcesDir);
        }
        return instance;
    }

    public static ResourcesUtil getExcitingInstance() {
        if (instance == null) throw new IllegalStateException("ResourcesUtil is not initialized");
        return instance;
    }

    public static File getByName(String name){
        Path path = Paths.get(instance.resourcesDir + name);
        return Files.exists(path) ? path.toFile() : null;
    }
}
