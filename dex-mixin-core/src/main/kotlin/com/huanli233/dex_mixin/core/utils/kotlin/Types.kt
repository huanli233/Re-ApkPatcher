package com.huanli233.dex_mixin.core.utils.kotlin

inline fun <reified T> Any.cast(): T {
    return this as T
}