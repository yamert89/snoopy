package com.github.yamert89.snoopy.compile;

import org.objectweb.asm.ClassReader;

import java.io.File;

public class InjectSqlClassExecutorFactory implements ClassExecutorFactory {
    @Override
    public ClassExecutor build(ClassReader reader, File classFile, ClassMetadata classMetadata) {
        return new InjectSqlExecutor(reader, classFile, classMetadata);
    }
}
