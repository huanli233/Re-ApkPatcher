package com.huanli233.dex_mixin.core.utils.dexlib2

import com.android.tools.smali.dexlib2.iface.AnnotationElement
import com.android.tools.smali.dexlib2.iface.reference.MethodReference
import com.android.tools.smali.dexlib2.iface.value.*
import com.huanli233.dex_mixin.core.utils.dexlib2.Converters.toValueType

fun EncodedValue.getByteValue() =
    (this as ByteEncodedValue).value

fun EncodedValue.getShortValue() =
    (this as ShortEncodedValue).value

fun EncodedValue.getCharValue() =
    (this as CharEncodedValue).value

fun EncodedValue.getIntValue() =
    (this as IntEncodedValue).value

fun EncodedValue.getLongValue() =
    (this as LongEncodedValue).value

fun EncodedValue.getFloatValue() =
    (this as FloatEncodedValue).value

fun EncodedValue.getDoubleValue() =
    (this as DoubleEncodedValue).value

fun EncodedValue.getMethodHandleValue() =
    (this as MethodHandleEncodedValue).value

fun EncodedValue.getMethodTypeValue() =
    (this as MethodTypeEncodedValue).value

fun EncodedValue.getStringValue() =
    (this as StringEncodedValue).value

fun EncodedValue.getTypeValue() =
   (this as TypeEncodedValue).value

fun EncodedValue.getFieldValue() =
    (this as FieldEncodedValue).value

fun EncodedValue.getMethodValue(): MethodReference =
    (this as MethodEncodedValue).value

fun EncodedValue.getEnumValue() =
    (this as EnumEncodedValue).value

fun EncodedValue.getArrayValue() =
    (this as ArrayEncodedValue).value

fun EncodedValue.getAnnotationValue() =
    (this as AnnotationEncodedValue).run {
        AnnotationValue(type, elements)
    }

fun EncodedValue.isNullValue() =
    valueType.toValueType() == ValueType.NULL

fun EncodedValue.getBooleanValue() =
    (this as BooleanEncodedValue).value

data class AnnotationValue(
    val type: String,
    val elements: Set<AnnotationElement>
)