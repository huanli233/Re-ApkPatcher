package com.huanli233.dex_mixin.core.utils.dexlib2

import com.android.tools.smali.dexlib2.iface.AnnotationElement
import com.huanli233.dex_mixin.api.annotations.Mixin
import com.huanli233.dex_mixin.api.annotations.SourceFile
import com.huanli233.dex_mixin.api.annotations.injection.At
import com.huanli233.dex_mixin.api.annotations.injection.At.Opcode
import com.huanli233.dex_mixin.api.annotations.injection.At.Pos
import com.huanli233.dex_mixin.api.annotations.injection.At.Shift
import com.huanli233.dex_mixin.api.annotations.injection.Desc
import com.huanli233.dex_mixin.api.annotations.injection.Inject
import com.huanli233.dex_mixin.api.annotations.injection.Redirect
import com.huanli233.dex_mixin.core.exceptions.DexMixinException
import com.huanli233.dex_mixin.core.utils.exception

object AnnotationValueParser {

    fun parseMixin(element: Set<AnnotationElement>): Mixin {
        val map = element.associateBy { it.name }
        return Mixin(
            value = map["value"]?.value?.getStringArrayValue() ?: emptyArray(),
            sourceFiles = map["sourceFiles"]?.value?.getArrayValue() { parseSourceFile(getAnnotationValue().elements) } ?: emptyArray(),
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

    fun parseDesc(element: Set<AnnotationElement>): Desc {
        val map = element.associateBy { it.name }
        return Desc(
            value = map["value"]?.value?.getStringArrayValue() ?: emptyArray(),
            regex = map["regex"]?.value?.getBooleanValue() == true,
            ret = map["ret"]?.value?.getStringValue() ?: "",
            args = map["args"]?.value?.getStringArrayValue() ?: emptyArray()
        )
    }

    fun parseAt(element: Set<AnnotationElement>): At {
        val map = element.associateBy { it.name }
        return At(
            value = map["value"]?.value?.getEnumValue()?.name?.let { Pos.valueOf(it) } ?: exception(),
            target = map["target"]?.value?.getStringValue() ?: "",
            shift = map["shift"]?.value?.getEnumValue()?.name?.let { Shift.valueOf(it) } ?: Shift.NONE,
            opcode = map["opcode"]?.value?.getEnumValue()?.name?.let { Opcode.valueOf(it) } ?: Opcode.ANY,
        )
    }

    fun parseInject(element: Set<AnnotationElement>): Inject {
        val map = element.associateBy { it.name }
        return Inject(
            method = map["method"]?.value?.getStringArrayValue() ?: emptyArray(),
            target = map["target"]?.value?.getArrayValue { parseDesc(getAnnotationValue().elements) } ?: emptyArray(),
            at = map["at"]?.value?.getAnnotationValue()?.let { parseAt(it.elements) } ?: exception(),
            cancellable = map["cancellable"]?.value?.getBooleanValue() == true
        )
    }

    fun parseRedirect(element: Set<AnnotationElement>): Redirect {
        val map = element.associateBy { it.name }
        return Redirect(
            method = map["method"]?.value?.getStringArrayValue() ?: emptyArray(),
            target = map["target"]?.value?.getArrayValue { parseDesc(getAnnotationValue().elements) } ?: emptyArray(),
            at = map["at"]?.value?.getAnnotationValue()?.let { parseAt(it.elements) } ?: exception()
        )
    }

}