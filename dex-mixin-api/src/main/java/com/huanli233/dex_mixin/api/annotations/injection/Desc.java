package com.huanli233.dex_mixin.api.annotations.injection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.CLASS)
public @interface Desc {

    public String[] value();

    public boolean regex() default false;

    public Class<?> ret() default void.class;

    public Class<?>[] args() default { };

    public int min() default 0;

    public int max() default Integer.MAX_VALUE;

}
