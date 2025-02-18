package com.huanli233.dex_mixin.core.utils.dexlib2

import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.iface.Annotation
import com.android.tools.smali.dexlib2.iface.ClassDef
import com.android.tools.smali.dexlib2.iface.Field
import com.android.tools.smali.dexlib2.iface.Method
import com.android.tools.smali.dexlib2.iface.instruction.Instruction
import com.android.tools.smali.dexlib2.iface.reference.FieldReference
import com.android.tools.smali.dexlib2.iface.reference.MethodReference
import com.android.tools.smali.dexlib2.immutable.reference.ImmutableMethodReference
import com.huanli233.dex_mixin.api.utils.smaliType
import com.huanli233.dex_mixin.core.utils.dexlib2.Converters.toAccessFlagsForClass
import com.huanli233.dex_mixin.core.utils.dexlib2.Converters.toAccessFlagsForField
import com.huanli233.dex_mixin.core.utils.dexlib2.Converters.toAccessFlagsForMethod
import kotlin.reflect.KClass

val Method.signature
    get() = "${name}(${parameterTypes.joinToString()})${returnType}"

val Method.description
    get() = "${definingClass}->$signature"

val MethodReference.signature
    get() = "${name}(${parameterTypes.joinToString()})${returnType}"

val MethodReference.description
    get() = "${definingClass}->$signature"

val Field.signature
    get() = "${name}:${type}"

val Field.description
    get() = "${definingClass}$signature"

val FieldReference.signature
    get() = "${name}:${type}"

val FieldReference.description
    get() = "${definingClass}$signature"

fun Collection<Instruction>.countCodeUnits() = sumOf { it.codeUnits }

fun Collection<Instruction>.getByAddress(address: Int): Pair<Int, Instruction>? {
    return associateBy({ it }, { it.codeUnits }).asSequence()
        .mapIndexed { index, (instruction, codeUnits) -> (index to instruction) to codeUnits }
        .firstOrNull { (pair, codeUnits) -> pair.first * codeUnits <= address && address < (pair.first + 1) * codeUnits }
        ?.first
}

fun Collection<Annotation>.getAnnotationOrNull(type: String) = firstOrNull { it.type == type }

fun Collection<Annotation>.getAnnotationOrNull(clazz: Class<*>) = getAnnotationOrNull(clazz.smaliType)

fun Collection<Annotation>.getAnnotationOrNull(clazz: KClass<*>) = getAnnotationOrNull(clazz.smaliType)

val Method.registerCountWithoutLocals
    get() =
        parameters.size + (if (accessFlags.toAccessFlagsForMethod().contains(AccessFlags.STATIC)) 0 else 1)

fun Method.parseAccessFlags() = accessFlags.toAccessFlagsForMethod()
fun Method.hasAccessFlags(vararg flags: AccessFlags) = flags.all { it in parseAccessFlags() }
fun Method.hasAnyAccessFlag(vararg flags: AccessFlags) = flags.any { it in parseAccessFlags() }

fun Field.parseAccessFlags() = accessFlags.toAccessFlagsForField()
fun Field.hasAccessFlags(vararg flags: AccessFlags) = flags.all { it in parseAccessFlags() }
fun Field.hasAnyAccessFlag(vararg flags: AccessFlags) = flags.any { it in parseAccessFlags() }

fun ClassDef.parseAccessFlags() = accessFlags.toAccessFlagsForClass()
fun ClassDef.hasAccessFlags(vararg flags: AccessFlags) = flags.all { it in parseAccessFlags() }
fun ClassDef.hasAnyAccessFlag(vararg flags: AccessFlags) = flags.any { it in parseAccessFlags() }

val Method.reference
    get() = ImmutableMethodReference(definingClass, name, parameters, returnType)