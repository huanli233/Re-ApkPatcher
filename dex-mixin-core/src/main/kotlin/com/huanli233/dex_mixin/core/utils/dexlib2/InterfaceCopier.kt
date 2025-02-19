package com.huanli233.dex_mixin.core.utils.dexlib2

import com.android.tools.smali.dexlib2.HiddenApiRestriction
import com.android.tools.smali.dexlib2.iface.Annotation
import com.android.tools.smali.dexlib2.iface.ExceptionHandler
import com.android.tools.smali.dexlib2.iface.Field
import com.android.tools.smali.dexlib2.iface.Method
import com.android.tools.smali.dexlib2.iface.MethodImplementation
import com.android.tools.smali.dexlib2.iface.MethodParameter
import com.android.tools.smali.dexlib2.iface.TryBlock
import com.android.tools.smali.dexlib2.iface.debug.DebugItem
import com.android.tools.smali.dexlib2.iface.instruction.Instruction
import com.android.tools.smali.dexlib2.iface.value.EncodedValue
import com.android.tools.smali.dexlib2.immutable.ImmutableField
import com.android.tools.smali.dexlib2.immutable.ImmutableMethod
import com.android.tools.smali.dexlib2.immutable.ImmutableMethodImplementation

fun Method.copy(
    definingClass: String = this.definingClass,
    name: String = this.name,
    parameters: List<MethodParameter> = this.parameters,
    returnType: String = this.returnType,
    accessFlags: Int = this.accessFlags,
    annotations: Set<Annotation> = this.annotations,
    hiddenApiRestrictions: Set<HiddenApiRestriction> = this.hiddenApiRestrictions,
    implementation: MethodImplementation? = this.implementation
) = ImmutableMethod(definingClass, name, parameters, returnType, accessFlags, annotations, hiddenApiRestrictions, implementation)

fun MethodImplementation.copy(
    registerCount: Int = this.registerCount,
    instructions: Iterable<Instruction> = this.instructions,
    tryBlocks: List<TryBlock<out ExceptionHandler>> = this.tryBlocks,
    debugItems: Iterable<DebugItem> = this.debugItems
) = ImmutableMethodImplementation(registerCount, instructions, tryBlocks, debugItems)

fun Field.copy(
    definingClass: String = this.definingClass,
    name: String = this.name,
    type: String = this.type,
    accessFlags: Int = this.accessFlags,
    initialValue: EncodedValue? = this.initialValue,
    annotations: Set<Annotation> = this.annotations,
    hiddenApiRestrictions: Set<HiddenApiRestriction> = this.hiddenApiRestrictions
) = ImmutableField(definingClass, name, type, accessFlags, initialValue, annotations, hiddenApiRestrictions)

fun <T> T.settable(
    setter: (T) -> Unit
) = Settable(this, setter)

class Settable<T>(
    initialValue: T,
    private val setter: (T) -> Unit
) {
    var value = initialValue
        set(value) {
            field = value
            setter(value)
        }
}