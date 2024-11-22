package com.huanli233.dex_mixin.api.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({})
@Retention(RetentionPolicy.CLASS)
public @interface SourceFile {

    String value();

    String extraRegex() default "";

    boolean regex() default false;

}
