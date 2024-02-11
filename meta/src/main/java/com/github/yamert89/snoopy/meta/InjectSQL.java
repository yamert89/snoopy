package com.github.yamert89.snoopy.meta;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a class to be scanned and that has fields that should be injected.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectSQL {
    /**
     * Specifies the prefix for the fields which should be injected.
     */
    String fieldsStartWith(); //todo default ?

    /**
     * Can be used for adding custom preprocessor for injected strings
     */
    Class<? extends Filter> filter() default EmptyFilter.class;

}
