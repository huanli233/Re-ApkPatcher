package com.huanli233.dex_mixin.api.utils

import kotlin.reflect.KClass

fun String.classToSmaliType() =
    SmaliUtil.javaClassToSmaliType(this)

val Class<*>.smaliType: String
    get() = SmaliUtil.javaClassToSmaliType(this)

val KClass<*>.smaliType: String
    get() = java.smaliType