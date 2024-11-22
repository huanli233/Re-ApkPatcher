package com.huanli233.dex_mixin.api.annotations.injection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.CLASS)
public @interface Inject {

    public String[] method() default {};

    public Desc[] target() default {};

    public At[] at();

    public boolean cancellable() default false;

}
