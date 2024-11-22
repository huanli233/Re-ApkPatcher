package com.huanli233.dex_mixin.api.annotations.injection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Modify method call parameters
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.CLASS)
public @interface ModifyArg {

    String[] method() default {};

    Desc[] target() default {};

    At at();

}
