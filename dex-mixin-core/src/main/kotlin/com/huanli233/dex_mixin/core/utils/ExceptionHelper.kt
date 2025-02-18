package com.huanli233.dex_mixin.core.utils

import com.huanli233.dex_mixin.core.exceptions.DexMixinException

fun exception(
    message: String? = null,
    cause: Throwable? = null
): Nothing {
    throw DexMixinException(message, cause)
}