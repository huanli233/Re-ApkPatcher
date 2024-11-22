package com.huanli233.dex_mixin.api.annotations.injection;

public @interface Redirect {

    public String[] method() default {};

    public Desc[] target() default {};

    public At at();

}
