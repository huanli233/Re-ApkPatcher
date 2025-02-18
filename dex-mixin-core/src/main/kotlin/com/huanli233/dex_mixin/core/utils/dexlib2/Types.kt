package com.huanli233.dex_mixin.core.utils.dexlib2

enum class SmaliType(val type: String) {
    VOID("V"),
    BOOLEAN("Z"),
    BYTE("B"),
    CHAR("C"),
    SHORT("S"),
    INT("I"),
    LONG("J"),
    FLOAT("F"),
    DOUBLE("D"),
    OBJECT("")
}

fun String.toSmaliType(): SmaliType
    = SmaliType.entries.firstOrNull { it.type == this } ?: SmaliType.OBJECT