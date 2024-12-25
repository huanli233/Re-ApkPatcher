package com.huanli233.dex_mixin.api.utils;

import org.jetbrains.annotations.NotNull;

public class SmaliUtil {

    @NotNull
    public static String javaClassToSmaliType(@NotNull String className) {
        return "L" + className.replace(".", "/") + ";";
    }

    @NotNull
    public static String javaClassToSmaliType(@NotNull Class<?> clazz) {
        return javaClassToSmaliType(clazz.getName());
    }

}
