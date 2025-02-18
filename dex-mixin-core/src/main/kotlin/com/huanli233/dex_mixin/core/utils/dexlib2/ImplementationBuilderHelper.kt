package com.huanli233.dex_mixin.core.utils.dexlib2

import com.android.tools.smali.dexlib2.builder.MethodImplementationBuilder

fun buildMethodImplementation(
    registerCount: Int,
    block: MethodImplementationBuilder.() -> Unit
) = MethodImplementationBuilder(registerCount).apply(block).methodImplementation