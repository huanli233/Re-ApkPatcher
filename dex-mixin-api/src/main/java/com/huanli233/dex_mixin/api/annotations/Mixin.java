package com.huanli233.dex_mixin.api.annotations;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
@Documented
public @interface Mixin {

    /**
     * Target class(es) for this mixin
     *
     * @return classes this mixin targets
     */
    Class<?>[] value() default { };

    /**
     * String type target class name(s) for this mixin
     *
     * @return target class name(s)
     */
    String[] targets() default { };
}
