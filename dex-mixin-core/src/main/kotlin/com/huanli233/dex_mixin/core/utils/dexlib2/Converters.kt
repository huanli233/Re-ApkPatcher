package com.huanli233.dex_mixin.core.utils.dexlib2

import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.AnnotationVisibility

object Converters {

    fun Int.toAnnotationVisibility(): String
        = AnnotationVisibility.getVisibility(this)

    fun Int.toAccessFlagsForClass(): Set<AccessFlags>
        = AccessFlags.getAccessFlagsForClass(this).toSet()

    fun Int.toAccessFlagsForMethod(): Set<AccessFlags>
        = AccessFlags.getAccessFlagsForMethod(this).toSet()

    fun Int.toAccessFlagsForField(): Set<AccessFlags>
        = AccessFlags.getAccessFlagsForField(this).toSet()

    fun Set<AccessFlags>.format(): String {
        val accessFlags = this.toTypedArray()

        var size = 0
        for (accessFlag in accessFlags) {
            size += accessFlag.toString().length + 1
        }

        val sb = StringBuilder(size)
        for (accessFlag in accessFlags) {
            sb.append(accessFlag.toString()).append(" ")
        }

        if (accessFlags.isNotEmpty()) {
            sb.deleteAt(sb.length - 1)
        }

        return sb.toString()
    }

    fun Int.hasAccessFlag(accessFlag: AccessFlags): Boolean
        = (accessFlag.value and this) == 0

    fun String.toAccessFlag(): AccessFlags
        = AccessFlags.getAccessFlag(this)

}