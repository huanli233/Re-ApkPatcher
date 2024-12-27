package com.huanli233.dex_mixin.core.dex

import com.android.tools.smali.dexlib2.iface.ClassDef
import com.huanli233.dex_mixin.api.annotations.Mixin
import com.huanli233.dex_mixin.api.utils.smaliTypeToJava
import com.huanli233.dex_mixin.core.exceptions.DexMixinException

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
            throw DexMixinException("${mixinClassType.smaliTypeToJava()} has no target or source file")
        }
    }

}