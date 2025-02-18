package com.huanli233.dex_mixin.core.utils.dexlib2

import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.iface.Method

object Preconditions {

    fun Method.checkReturnType(other: Method)
        = returnType == other.returnType

    fun Method.checkStatic(other: Method)
        = hasAccessFlags(AccessFlags.STATIC) == other.hasAccessFlags(AccessFlags.STATIC)

    fun Method.checkParameters(other: Method)
        = parameterTypes == other.parameterTypes

}