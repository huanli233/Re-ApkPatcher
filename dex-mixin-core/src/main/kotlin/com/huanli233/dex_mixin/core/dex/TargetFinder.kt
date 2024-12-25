package com.huanli233.dex_mixin.core.dex

import com.android.tools.smali.dexlib2.iface.AnnotationElement
import com.android.tools.smali.dexlib2.iface.ClassDef

object TargetFinder {

    fun findTargetByMixinAnnotation(
        annotationElements: Set<AnnotationElement>,
        classDefs: Set<ClassDef>
    ): ClassDef? {
        TODO("not implemented")
    }

}