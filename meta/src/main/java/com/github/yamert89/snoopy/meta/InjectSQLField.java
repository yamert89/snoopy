package com.github.yamert89.snoopy.meta;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a single field to be scanned and that should be injected.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
public @interface InjectSQLField {

    /**
     * The name of appropriate resource (*.sql file)
     */
    String name();

    /**
     * Can be used for adding custom preprocessor for injected strings
     */
    Class<? extends Filter> filter() default EmptyFilter.class;
}
