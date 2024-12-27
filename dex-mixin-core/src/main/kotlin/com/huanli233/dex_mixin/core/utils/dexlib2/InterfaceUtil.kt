package com.huanli233.dex_mixin.core.utils.dexlib2

import com.android.tools.smali.dexlib2.iface.Field
import com.android.tools.smali.dexlib2.iface.Method

val Method.signature
    get() = "${name}(${parameters.joinToString { it.type }})${returnType}"

val Field.signature
    get() = "${name}:${type}"