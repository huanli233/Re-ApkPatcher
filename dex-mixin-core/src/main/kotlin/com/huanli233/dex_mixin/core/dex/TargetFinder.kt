package com.huanli233.dex_mixin.core.dex

import com.android.tools.smali.dexlib2.iface.Annotation
import com.android.tools.smali.dexlib2.iface.ClassDef
import com.android.tools.smali.dexlib2.iface.Method
import com.huanli233.dex_mixin.api.annotations.Mixin
import com.huanli233.dex_mixin.api.annotations.injection.Desc
import com.huanli233.dex_mixin.api.annotations.injection.Inject
import com.huanli233.dex_mixin.api.utils.smaliTypeToJava
import com.huanli233.dex_mixin.core.exceptions.DexMixinException
import com.huanli233.dex_mixin.core.utils.dexlib2.AnnotationValueParser
import com.huanli233.dex_mixin.core.utils.dexlib2.getAnnotationValue
import com.huanli233.dex_mixin.core.utils.dexlib2.getArrayValue
import com.huanli233.dex_mixin.core.utils.dexlib2.getStringArrayValue
import com.huanli233.dex_mixin.core.utils.dexlib2.getStringValue
import com.huanli233.dex_mixin.core.utils.dexlib2.signature
import com.huanli233.dex_mixin.core.utils.exception

object TargetFinder {

    fun findTargetByMixinAnnotation(
        mixinClassType: String,
        mixin: Mixin,
        classDefs: Set<ClassDef>
    ): ClassDef? {
        if (mixin.value.isNotEmpty()) {
            return classDefs.find { def ->
                if (mixin.targetRegex) {
                    mixin.value.any { string ->
                        def.type.smaliTypeToJava().matches(string.toRegex())
                    }
                } else {
                    def.type.smaliTypeToJava() in mixin.value
                }
            }
        } else if (mixin.sourceFiles.isNotEmpty()) {
            return classDefs.find { def ->
                mixin.sourceFiles.any { source ->
                    if (source.regex) {
                        def.sourceFile?.matches(source.value.toRegex()) == true
                    } else {
                        def.sourceFile == source.value
                    } && source.typeRegex.let { regex ->
                        if (regex.isNotEmpty()) {
                            def.type.matches(regex.toRegex())
                        } else true
                    }
                }
            }
        } else {
            exception("${mixinClassType.smaliTypeToJava()} has no target or source file")
        }
    }

    fun findTargetMethodByAnnotations(
        annotations: Set<Annotation>,
        methods: Set<Method>
    ): Method? {
        annotations.forEach { annotation ->
            val methodSignatures = annotation.getMethodSignatures()
            return if (methodSignatures.isNotEmpty()) {
                methods.find { method ->
                    methodSignatures.any { it == method.signature }
                }
            } else {
                val desc = annotation.getDesc()
                methods.find { method ->
                    desc.any {
                        it.value.any { name ->
                            if (it.regex) {
                                method.name.matches(name.toRegex())
                            } else {
                                method.name == name
                            } && method.returnType == it.ret && method.parameterTypes.toTypedArray() == it.args
                        }
                    }
                }
            }
        }
        return null
    }

    private fun Annotation.getMethodSignatures(): Set<String> =
        elements.find { element -> element.name == "method" }?.value?.getStringArrayValue()?.toSet() ?: emptySet()

    private fun Annotation.getDesc(): Set<Desc> =
        elements.find { element -> element.name == "desc" }?.value?.getArrayValue() { AnnotationValueParser.parseDesc(getAnnotationValue().elements) }?.toSet() ?: emptySet()

}