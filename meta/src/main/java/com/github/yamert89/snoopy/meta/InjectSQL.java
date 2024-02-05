package com.github.yamert89.snoopy.meta;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectSQL {
    String fieldsStartWith(); //todo default ?

    Class<? extends Filter> filter() default EmptyFilter.class;

}
