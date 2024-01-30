package yamert89.snoopy.compile;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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
        Optional<File> file = resources.stream().filter(f -> f.getName().equals(name)).findFirst();
        if (file.isEmpty()) throw new NoSuchElementException(String.format("File %s not found in %s", name, resources));

        return file.get();
    }
}
