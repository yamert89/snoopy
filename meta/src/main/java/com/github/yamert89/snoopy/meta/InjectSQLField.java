package com.github.yamert89.snoopy.meta;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
public @interface InjectSQLField {
    String name() default "";

    Class<? extends Filter> filter() default EmptyFilter.class;
}
