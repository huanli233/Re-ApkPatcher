package com.huanli233.dex_mixin.api.annotations.injection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Modify multiple parameters of a method call
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.CLASS)
public @interface ModifyArgs {

    public String[] method() default {};

    public Desc[] target() default {};

    public At at();

}
