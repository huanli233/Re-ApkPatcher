package com.huanli233.dex_mixin.core.utils

import java.io.InputStream
import java.util.zip.ZipFile

fun ZipFile.getEntryInputStream(
    name: String
): InputStream? {
    return getEntry(name)?.let { getInputStream(it) }
}