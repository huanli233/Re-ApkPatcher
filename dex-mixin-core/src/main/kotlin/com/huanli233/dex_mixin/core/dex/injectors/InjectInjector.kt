package com.huanli233.dex_mixin.core.dex.injectors

import com.android.tools.smali.dexlib2.iface.Method
import com.huanli233.dex_mixin.api.annotations.injection.Inject
import com.huanli233.dex_mixin.core.dex.Injector
import com.huanli233.dex_mixin.core.utils.dexlib2.AnnotationValueParser

object InjectInjector: Injector() {

    override fun inject(
        method: Method,
        target: Method
    ): Method? {
        return method.getInjectInfo(Inject::class.java) {
            AnnotationValueParser.parseInject(it)
        }.let body@{ inject ->
            TODO()
        }
    }

}