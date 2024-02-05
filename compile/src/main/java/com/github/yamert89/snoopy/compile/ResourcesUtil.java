package com.github.yamert89.snoopy.compile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class ResourcesUtil {
    private final List<File> resources;
    private static ResourcesUtil instance;
    private final Logger log = LoggerFactory.getLogger(ResourcesUtil.class);

    private ResourcesUtil(List<File> resources) {
        this.resources = resources;
        log.debug("ResourcesUtil was initialized with files: " + resources);
    }

    public static void initialize(List<File> resources) { //TODO DI ?
        instance = new ResourcesUtil(Collections.unmodifiableList(resources));
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
