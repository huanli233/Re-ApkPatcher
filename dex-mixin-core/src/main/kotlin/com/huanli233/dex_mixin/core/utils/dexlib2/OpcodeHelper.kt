package com.huanli233.dex_mixin.core.utils.dexlib2

import com.android.tools.smali.dexlib2.AccessFlags.*
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.Method

fun Method.getInvokeOpcode(isRange: Boolean = false): Opcode {
    return if (hasAccessFlags(STATIC)) {
        if (isRange) Opcode.INVOKE_STATIC_RANGE else Opcode.INVOKE_STATIC
    } else if (hasAccessFlags(INTERFACE)) {
        if (isRange) Opcode.INVOKE_INTERFACE_RANGE else Opcode.INVOKE_INTERFACE
    } else if (hasAnyAccessFlag(PROTECTED, PUBLIC) && hasAccessFlags(CONSTRUCTOR).not()) {
        if (isRange) Opcode.INVOKE_VIRTUAL_RANGE else Opcode.INVOKE_VIRTUAL
    } else {
        if (isRange) Opcode.INVOKE_DIRECT_RANGE else Opcode.INVOKE_DIRECT
    }
}