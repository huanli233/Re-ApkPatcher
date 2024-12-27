package com.huanli233.dex_mixin.api.annotations;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Mixin {

    /**
     * String type target class name(s) for this mixin
     *
     * @return target class name(s)
     */
    String[] value() default { };

    SourceFile[] sourceFiles() default { };

    boolean targetRegex() default false;

}
