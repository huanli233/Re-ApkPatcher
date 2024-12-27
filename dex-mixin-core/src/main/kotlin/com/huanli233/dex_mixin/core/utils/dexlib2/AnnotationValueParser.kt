package com.huanli233.dex_mixin.core.utils.dexlib2

import com.android.tools.smali.dexlib2.iface.AnnotationElement
import com.huanli233.dex_mixin.api.annotations.Mixin
import com.huanli233.dex_mixin.api.annotations.SourceFile

object AnnotationValueParser {

    fun parseMixin(element: Set<AnnotationElement>): Mixin {
        val map = element.associateBy { it.name }
        return Mixin(
            value = map["value"]?.value?.getArrayValue()?.map { it.getStringValue() }?.toTypedArray() ?: arrayOf(),
            sourceFiles = map["sourceFiles"]?.value?.getArrayValue()?.map { parseSourceFile(it.getAnnotationValue().elements) }?.toTypedArray() ?: arrayOf(),
            targetRegex = map["targetRegex"]?.value?.getBooleanValue() == true
        )
    }

    fun parseSourceFile(element: Set<AnnotationElement>): SourceFile {
        val map = element.associateBy { it.name }
        return SourceFile(
            value = map["value"]?.value?.getStringValue() ?: "",
            typeRegex = map["typeRegex"]?.value?.getStringValue() ?: "",
            regex = map["regex"]?.value?.getBooleanValue() == true
        )
    }

}