package com.huanli233.dex_mixin.core.dex

import com.android.tools.smali.dexlib2.iface.Method
import com.huanli233.dex_mixin.api.annotations.injection.Inject
import com.huanli233.dex_mixin.api.annotations.injection.ModifyArg
import com.huanli233.dex_mixin.api.annotations.injection.ModifyArgs
import com.huanli233.dex_mixin.api.annotations.injection.ModifyConstant
import com.huanli233.dex_mixin.api.annotations.injection.ModifyVariable
import com.huanli233.dex_mixin.api.annotations.injection.Redirect
import com.huanli233.dex_mixin.api.utils.smaliType
import com.huanli233.dex_mixin.core.dex.injectors.InjectInjector
import com.huanli233.dex_mixin.core.dex.injectors.RedirectInjector

object MethodInjection {

    val injectors = mapOf(
        Inject::class.java to InjectInjector,
//        ModifyArg::class.java,
//        ModifyArgs::class.java,
//        ModifyConstant::class.java,
//        ModifyVariable::class.java,
        Redirect::class.java to RedirectInjector
    )

    fun Method.isInjectMethod(): Boolean =
        annotations.any { injectors.any { entry -> entry.key.smaliType == it.type } }

    fun Method.injectTo(other: Method): Method {
        return injectors.firstNotNullOf { if (other.annotations.any { annotation -> annotation.type == it.key.smaliType }) it.value else null }.inject(this, other) ?: other
    }

}