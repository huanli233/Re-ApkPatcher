package com.huanli233.dex_mixin.core.utils.dexlib2

import com.android.tools.smali.dexlib2.Opcode

val INVOKE_OPCODES = arrayOf(
    Opcode.INVOKE_STATIC, Opcode.INVOKE_DIRECT, Opcode.INVOKE_VIRTUAL, Opcode.INVOKE_SUPER,
    Opcode.INVOKE_INTERFACE, Opcode.INVOKE_VIRTUAL_RANGE, Opcode.INVOKE_INTERFACE_RANGE, Opcode.INVOKE_SUPER_RANGE, Opcode.INVOKE_DIRECT_RANGE, Opcode.INVOKE_STATIC_RANGE
)

val STATIC_GET_OPCODES = arrayOf(
    Opcode.SGET, Opcode.SGET_WIDE, Opcode.SGET_OBJECT, Opcode.SGET_BOOLEAN, Opcode.SGET_BYTE, Opcode.SGET_CHAR, Opcode.SGET_SHORT
)

val INSTANCE_GET_OPCODES = arrayOf(
    Opcode.IGET, Opcode.IGET_WIDE, Opcode.IGET_OBJECT, Opcode.IGET_BOOLEAN, Opcode.IGET_BYTE, Opcode.IGET_CHAR, Opcode.IGET_SHORT
)

val GET_FIELD_OPCODES = arrayOf(*STATIC_GET_OPCODES, *INSTANCE_GET_OPCODES)

val STATIC_PUT_OPCODES = arrayOf(
    Opcode.SPUT, Opcode.SPUT_WIDE, Opcode.SPUT_OBJECT, Opcode.SPUT_BOOLEAN, Opcode.SPUT_BYTE, Opcode.SPUT_CHAR, Opcode.SPUT_SHORT
)

val INSTANCE_PUT_OPCODES = arrayOf(
    Opcode.IPUT, Opcode.IPUT_WIDE, Opcode.IPUT_OBJECT, Opcode.IPUT_BOOLEAN, Opcode.IPUT_BYTE, Opcode.IPUT_CHAR, Opcode.IPUT_SHORT
)

val PUT_FIELD_OPCODES = arrayOf(*STATIC_PUT_OPCODES, *INSTANCE_PUT_OPCODES)

val FIELD_OPCODES = arrayOf(*GET_FIELD_OPCODES, *PUT_FIELD_OPCODES)