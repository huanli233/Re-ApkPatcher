package com.huanli233.dex_mixin.api.annotations.injection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.CLASS)
public @interface ModifyVariable {

    String[] method() default {};

    Desc[] target() default {};

    boolean store() default false;

    int ordinal() default -1;

}
