package com.github.yamert89.snoopy.compile;

import java.io.File;
import java.util.List;

/**
 * Here is the entry point to the public API
 */
public interface ClassModifier {

    /**
     * Receives the collection of class files and the collection of .sql scripts,
     * modifies the classes
     */
    void modify(List<File> classes, List<File> resources);
}
