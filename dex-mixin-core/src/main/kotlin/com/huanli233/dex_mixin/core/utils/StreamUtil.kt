package com.huanli233.dex_mixin.core.utils

import java.io.InputStream
import java.io.InputStreamReader

fun InputStream.withReader(
    block: (InputStreamReader) -> Unit
) = use { inputStream ->
    InputStreamReader(inputStream).use { reader ->
        block(reader)
    }
}