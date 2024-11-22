package com.huanli233.dex_mixin.api.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD, ElementType.FIELD})
@Documented
public @interface Shadow {
}
