package com.huanli233.dex_mixin.core.utils

import com.huanli233.dex_mixin.core.exceptions.DexMixinException
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

fun exception(
    message: String? = null,
    cause: Throwable? = null
): Nothing {
    throw DexMixinException(message, cause)
}

@OptIn(ExperimentalContracts::class)
inline fun require(value: Boolean, lazyMessage: () -> Any) {
    contract {
        returns() implies value
    }
    if (!value) {
        val message = lazyMessage()
        throw DexMixinException(message.toString())
    }
}