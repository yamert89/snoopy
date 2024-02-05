package com.github.yamert89.snoopy.compile;

import org.objectweb.asm.ClassReader;

import java.io.File;

public interface ClassExecutorFactory {
    ClassExecutor build(ClassReader reader, File classFile, ClassMetadata classMetadata);
}
