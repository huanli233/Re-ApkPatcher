package com.huanli233.dex_mixin.api.annotations.injection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.CLASS)
public @interface ModifyVariable {

    public String[] method() default {};

    public Desc[] target() default {};

    public boolean store() default false;

    public int ordinal() default -1;

}
