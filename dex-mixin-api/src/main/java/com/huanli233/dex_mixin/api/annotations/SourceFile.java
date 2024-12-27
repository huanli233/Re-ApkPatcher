package com.huanli233.dex_mixin.api.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({})
@Retention(RetentionPolicy.RUNTIME)
public @interface SourceFile {

    String value() default "";

    String typeRegex() default "";

    boolean regex() default false;

}
