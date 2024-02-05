package com.github.yamert89.snoopy.compile;

import java.io.File;
import java.util.List;

public interface ClassModifier {
    void modify(List<File> classes, List<File> resources);
}
