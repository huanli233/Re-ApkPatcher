package com.huanli233.dex_mixin.core.dex

import com.android.tools.smali.dexlib2.iface.AnnotationElement
import com.android.tools.smali.dexlib2.iface.Method
import com.huanli233.dex_mixin.core.utils.dexlib2.getAnnotationOrNull

abstract class Injector {

    abstract fun inject(method: Method, target: Method): Method?

    protected fun <T> Method.getInjectInfo(
        clazz: Class<T>,
        parser: (Set<AnnotationElement>) -> T
    ): T {
        return annotations.getAnnotationOrNull(clazz)?.elements?.let(parser)!!
    }

}