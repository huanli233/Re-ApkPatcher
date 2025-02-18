package com.huanli233.dex_mixin.core.utils.dexlib2

import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.AnnotationVisibility
import com.android.tools.smali.dexlib2.ValueType
import com.android.tools.smali.dexlib2.DebugItemType
import com.android.tools.smali.dexlib2.ReferenceType
import com.android.tools.smali.dexlib2.iface.reference.Reference

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

    fun Int.toValueTypeName(): String =
        ValueType.getValueTypeName(this)

    fun Int.toValueType(): com.huanli233.dex_mixin.core.utils.dexlib2.ValueType =
        when (this.toValueTypeName()) {
            "byte" -> com.huanli233.dex_mixin.core.utils.dexlib2.ValueType.BYTE
            "short" -> com.huanli233.dex_mixin.core.utils.dexlib2.ValueType.SHORT
            "char" -> com.huanli233.dex_mixin.core.utils.dexlib2.ValueType.CHAR
            "int" -> com.huanli233.dex_mixin.core.utils.dexlib2.ValueType.INT
            "long" -> com.huanli233.dex_mixin.core.utils.dexlib2.ValueType.LONG
            "float" -> com.huanli233.dex_mixin.core.utils.dexlib2.ValueType.FLOAT
            "double" -> com.huanli233.dex_mixin.core.utils.dexlib2.ValueType.DOUBLE
            "boolean" -> com.huanli233.dex_mixin.core.utils.dexlib2.ValueType.BOOLEAN
            "method_type" -> com.huanli233.dex_mixin.core.utils.dexlib2.ValueType.METHOD_TYPE
            "method_handle" -> com.huanli233.dex_mixin.core.utils.dexlib2.ValueType.METHOD_HANDLE
            "string" -> com.huanli233.dex_mixin.core.utils.dexlib2.ValueType.STRING
            "type" -> com.huanli233.dex_mixin.core.utils.dexlib2.ValueType.TYPE
            "field" -> com.huanli233.dex_mixin.core.utils.dexlib2.ValueType.FIELD
            "method" -> com.huanli233.dex_mixin.core.utils.dexlib2.ValueType.METHOD
            "enum" -> com.huanli233.dex_mixin.core.utils.dexlib2.ValueType.ENUM
            "annotation" -> com.huanli233.dex_mixin.core.utils.dexlib2.ValueType.ANNOTATION
            "array" -> com.huanli233.dex_mixin.core.utils.dexlib2.ValueType.ARRAY
            "null" -> com.huanli233.dex_mixin.core.utils.dexlib2.ValueType.NULL
            else -> com.huanli233.dex_mixin.core.utils.dexlib2.ValueType.NULL
        }

    fun Int.toDebugItemType(): com.huanli233.dex_mixin.core.utils.dexlib2.DebugItemType {
        return when (this) {
            DebugItemType.START_LOCAL -> com.huanli233.dex_mixin.core.utils.dexlib2.DebugItemType.START_LOCAL
            DebugItemType.END_LOCAL -> com.huanli233.dex_mixin.core.utils.dexlib2.DebugItemType.END_LOCAL
            DebugItemType.RESTART_LOCAL -> com.huanli233.dex_mixin.core.utils.dexlib2.DebugItemType.RESTART_LOCAL
            DebugItemType.PROLOGUE_END -> com.huanli233.dex_mixin.core.utils.dexlib2.DebugItemType.PROLOGUE_END
            DebugItemType.EPILOGUE_BEGIN -> com.huanli233.dex_mixin.core.utils.dexlib2.DebugItemType.EPILOGUE_BEGIN
            DebugItemType.SET_SOURCE_FILE -> com.huanli233.dex_mixin.core.utils.dexlib2.DebugItemType.SET_SOURCE_FILE
            DebugItemType.LINE_NUMBER -> com.huanli233.dex_mixin.core.utils.dexlib2.DebugItemType.LINE_NUMBER
            DebugItemType.END_SEQUENCE -> com.huanli233.dex_mixin.core.utils.dexlib2.DebugItemType.END_SEQUENCE
            DebugItemType.ADVANCE_PC -> com.huanli233.dex_mixin.core.utils.dexlib2.DebugItemType.ADVANCE_PC
            DebugItemType.ADVANCE_LINE -> com.huanli233.dex_mixin.core.utils.dexlib2.DebugItemType.ADVANCE_LINE
            DebugItemType.START_LOCAL_EXTENDED -> com.huanli233.dex_mixin.core.utils.dexlib2.DebugItemType.START_LOCAL_EXTENDED
            else -> com.huanli233.dex_mixin.core.utils.dexlib2.DebugItemType.UNKNOWN
        }
    }

    fun Int.toReferenceType(): com.huanli233.dex_mixin.core.utils.dexlib2.ReferenceType {
        return when (this) {
            ReferenceType.STRING -> com.huanli233.dex_mixin.core.utils.dexlib2.ReferenceType.STRING
            ReferenceType.TYPE -> com.huanli233.dex_mixin.core.utils.dexlib2.ReferenceType.TYPE
            ReferenceType.METHOD -> com.huanli233.dex_mixin.core.utils.dexlib2.ReferenceType.METHOD
            ReferenceType.FIELD -> com.huanli233.dex_mixin.core.utils.dexlib2.ReferenceType.FIELD
            ReferenceType.METHOD_PROTO -> com.huanli233.dex_mixin.core.utils.dexlib2.ReferenceType.METHOD_PROTO
            ReferenceType.CALL_SITE -> com.huanli233.dex_mixin.core.utils.dexlib2.ReferenceType.CALL_SITE
            ReferenceType.METHOD_HANDLE -> com.huanli233.dex_mixin.core.utils.dexlib2.ReferenceType.METHOD_HANDLE
            else -> com.huanli233.dex_mixin.core.utils.dexlib2.ReferenceType.NONE
        }
    }

    val Reference.referenceType
        get() = ReferenceType.getReferenceType(this).toReferenceType()

}

enum class ValueType {
    BYTE, SHORT, CHAR, INT, LONG, FLOAT, DOUBLE, BOOLEAN, METHOD_TYPE, METHOD_HANDLE, STRING, TYPE, FIELD, METHOD, ENUM, ANNOTATION, ARRAY, NULL
}

enum class DebugItemType {
    START_LOCAL, END_LOCAL, RESTART_LOCAL,
    PROLOGUE_END, EPILOGUE_BEGIN,
    SET_SOURCE_FILE, LINE_NUMBER,

    END_SEQUENCE, ADVANCE_PC, ADVANCE_LINE, START_LOCAL_EXTENDED,

    UNKNOWN
}

enum class ReferenceType {
    STRING, TYPE, FIELD, METHOD, METHOD_PROTO,

    CALL_SITE, METHOD_HANDLE, NONE
}
