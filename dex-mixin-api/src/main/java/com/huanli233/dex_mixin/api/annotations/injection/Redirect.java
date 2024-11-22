package com.huanli233.dex_mixin.api.annotations.injection;

public @interface Redirect {

    String[] method() default {};

    Desc[] target() default {};

    At at();

}
