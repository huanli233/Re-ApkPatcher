package com.huanli233.dex_mixin.core.exceptions

class DexMixinException(
    message: String? = null,
    cause: Throwable? = null
) : Exception(message, cause) {

    override fun toString(): String {
        return "DexMixinException: ${message ?: "Unknown error"}"
    }

}