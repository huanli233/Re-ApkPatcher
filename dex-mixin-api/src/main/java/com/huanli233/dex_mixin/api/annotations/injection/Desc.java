package com.huanli233.dex_mixin.api.annotations.injection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.CLASS)
public @interface Desc {

    String[] value();

    boolean regex() default false;

    Class<?> ret() default void.class;

    Class<?>[] args() default { };

    int min() default 0;

    int max() default Integer.MAX_VALUE;

}
