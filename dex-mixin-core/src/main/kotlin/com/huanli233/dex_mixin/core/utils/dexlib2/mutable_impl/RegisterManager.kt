package com.huanli233.dex_mixin.core.utils.dexlib2.mutable_impl

import com.android.tools.smali.dexlib2.iface.Method
import com.android.tools.smali.dexlib2.iface.MethodImplementation
import com.huanli233.dex_mixin.core.utils.dexlib2.Settable
import com.huanli233.dex_mixin.core.utils.dexlib2.description
import com.huanli233.dex_mixin.core.utils.dexlib2.registerCountWithoutLocals

object RegisterManager {

    val allocatedRegistersForMethods = mutableMapOf<String, Int>()

    fun MethodImplementation.getRegisters(
        method: Method,
        registerCountSettable: Settable<Int>,
        count: Int
    ): Set<Int> {
        synchronized(this) {
            val registerCount = registerCount
            val methodDesc = method.description
            var createdRegisterCount = allocatedRegistersForMethods[methodDesc] ?: 0
            if (count > createdRegisterCount) {
                registerCountSettable.value = registerCount + (count - createdRegisterCount)
                createdRegisterCount = count
            }
            allocatedRegistersForMethods[methodDesc] = createdRegisterCount
            val registerCountWithoutLocals = method.registerCountWithoutLocals
            return ((registerCount - createdRegisterCount - registerCountWithoutLocals)..(registerCount - createdRegisterCount + count - 1 - registerCountWithoutLocals)).toSet()
        }
    }

    fun getTempRegisters(modifier: MethodImplementationModifier, i: Int): Array<Int> {
        TODO("not implemented")
    }

}