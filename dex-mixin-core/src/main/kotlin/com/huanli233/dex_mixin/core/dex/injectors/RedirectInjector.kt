package com.huanli233.dex_mixin.core.dex.injectors

import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction11x
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction35c
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction3rc
import com.android.tools.smali.dexlib2.iface.Method
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction21c
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction22c
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction35c
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction3rc
import com.android.tools.smali.dexlib2.iface.reference.FieldReference
import com.android.tools.smali.dexlib2.iface.reference.MethodReference
import com.huanli233.dex_mixin.api.annotations.injection.At
import com.huanli233.dex_mixin.api.annotations.injection.Redirect
import com.huanli233.dex_mixin.core.dex.Injector
import com.huanli233.dex_mixin.core.utils.dexlib2.*
import com.huanli233.dex_mixin.core.utils.dexlib2.mutable_impl.modifyImplementation
import com.huanli233.dex_mixin.core.utils.exception

object RedirectInjector : Injector() {

    override fun inject(
        method: Method,
        target: Method
    ): Method? {
        return method.getInjectInfo(Redirect::class.java) {
            AnnotationValueParser.parseRedirect(it)
        }.let body@{ redirect ->
            when (redirect.at.value) {
                At.Pos.INVOKE -> {
                    target.implementation!!.instructions.forEachIndexed { index, instruction ->

                        if (instruction.opcode in INVOKE_OPCODES && instruction is ReferenceInstruction) {
                            (instruction.reference as? MethodReference)?.let { reference ->

                                if (reference.description == redirect.at.target) {
                                    require(method.parameterTypes != reference.parameterTypes) { exception("Method parameter types not match") }
                                    return@body target.copy(
                                        implementation = target.modifyImplementation {
                                            replaceInstruction(
                                                index,
                                                if (instruction is Instruction35c) {
                                                    BuilderInstruction35c(
                                                        method.getInvokeOpcode(),
                                                        instruction.registerCount,
                                                        instruction.registerC,
                                                        instruction.registerD,
                                                        instruction.registerE,
                                                        instruction.registerF,
                                                        instruction.registerG,
                                                        method.reference
                                                    )
                                                } else if (instruction is Instruction3rc) {
                                                    BuilderInstruction3rc(
                                                        method.getInvokeOpcode(true),
                                                        instruction.registerCount,
                                                        instruction.startRegister,
                                                        method.reference
                                                    )
                                                } else exception()
                                            )
                                        }
                                    )
                                }

                            }
                        }

                    }
                    null
                }

                At.Pos.FIELD -> {
                    var offset = 0
                    target.implementation!!.instructions.forEachIndexed { originalIndex, instruction ->
                        val index = originalIndex + offset
                        if (instruction.opcode in FIELD_OPCODES &&
                            instruction is ReferenceInstruction
                        ) {

                            (instruction.reference as? FieldReference)?.let { reference ->

                                if (reference.description == redirect.at.target) {
                                    return@body target.copy(
                                        implementation = target.modifyImplementation {
                                            val isGetFieldOpcode = instruction.opcode in GET_FIELD_OPCODES
                                            val isPutFieldOpcode = instruction.opcode in PUT_FIELD_OPCODES
                                            if (redirect.at.opcode == At.Opcode.ANY ||
                                                (redirect.at.opcode == At.Opcode.GET_VALUE && isGetFieldOpcode) ||
                                                (redirect.at.opcode == At.Opcode.PUT_VALUE && isPutFieldOpcode)
                                            ) {
                                                when (instruction) {
                                                    is Instruction22c -> {
                                                        val expectedParameterTypes =
                                                            if (isGetFieldOpcode) listOf(reference.definingClass) else listOf(
                                                                reference.definingClass,
                                                                reference.type
                                                            )
                                                        require(method.parameterTypes == expectedParameterTypes) {
                                                            exception(
                                                                "Parameter type is illegal. Expected: [${expectedParameterTypes.joinToString()}]"
                                                            )
                                                        }
                                                        val expectedReturnType =
                                                            if (isGetFieldOpcode) reference.type else SmaliType.VOID.type
                                                        require(method.returnType == expectedReturnType) { exception("Return type is illegal. Expected: [$expectedReturnType]") }
                                                        if (isGetFieldOpcode) {
                                                            replaceInstruction(
                                                                index,
                                                                BuilderInstruction35c(
                                                                    method.getInvokeOpcode(),
                                                                    1,
                                                                    instruction.registerB,
                                                                    0,
                                                                    0,
                                                                    0,
                                                                    0,
                                                                    method.reference
                                                                )
                                                            )
                                                            addInstruction(
                                                                index + 1,
                                                                BuilderInstruction11x(
                                                                    if (instruction.opcode == Opcode.IGET_WIDE) Opcode.MOVE_RESULT_WIDE
                                                                    else if (method.returnType.toSmaliType() == SmaliType.OBJECT) Opcode.MOVE_RESULT_OBJECT
                                                                    else Opcode.MOVE_RESULT,
                                                                    instruction.registerA
                                                                )
                                                            )
                                                            offset++
                                                        } else if (isPutFieldOpcode) {
                                                            replaceInstruction(
                                                                index,
                                                                BuilderInstruction35c(
                                                                    method.getInvokeOpcode(),
                                                                    2,
                                                                    instruction.registerB,
                                                                    instruction.registerA,
                                                                    0,
                                                                    0,
                                                                    0,
                                                                    method.reference
                                                                )
                                                            )
                                                        }
                                                    }

                                                    is Instruction21c -> {
                                                        val expectedParameterTypes =
                                                            if (isGetFieldOpcode) emptyList<String>() else listOf(
                                                                reference.type
                                                            )
                                                        require(method.parameterTypes == expectedParameterTypes) {
                                                            exception(
                                                                "Parameter type is illegal. Expected: [${expectedParameterTypes.joinToString()}]"
                                                            )
                                                        }
                                                        val expectedReturnType =
                                                            if (isGetFieldOpcode) reference.type else SmaliType.VOID.type
                                                        require(method.returnType == expectedReturnType) { exception("Return type is illegal. Expected: [$expectedReturnType]") }
                                                        if (isGetFieldOpcode) {
                                                            replaceInstruction(
                                                                index,
                                                                BuilderInstruction35c(
                                                                    method.getInvokeOpcode(),
                                                                    0,
                                                                    0,
                                                                    0,
                                                                    0,
                                                                    0,
                                                                    0,
                                                                    method.reference
                                                                )
                                                            )
                                                            addInstruction(
                                                                index + 1,
                                                                BuilderInstruction11x(
                                                                    if (instruction.opcode == Opcode.IGET_WIDE) Opcode.MOVE_RESULT_WIDE
                                                                    else if (method.returnType.toSmaliType() == SmaliType.OBJECT) Opcode.MOVE_RESULT_OBJECT
                                                                    else Opcode.MOVE_RESULT,
                                                                    instruction.registerA
                                                                )
                                                            )
                                                            offset++
                                                        } else if (isPutFieldOpcode) {
                                                            replaceInstruction(
                                                                index,
                                                                BuilderInstruction35c(
                                                                    method.getInvokeOpcode(),
                                                                    1,
                                                                    instruction.registerA,
                                                                    0,
                                                                    0,
                                                                    0,
                                                                    0,
                                                                    method.reference
                                                                )
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    )
                                }

                            }

                        }
                    }
                    null
                }

                else -> exception("Unsupported redirect type: ${redirect.at.value}")
            }
        }
    }

}