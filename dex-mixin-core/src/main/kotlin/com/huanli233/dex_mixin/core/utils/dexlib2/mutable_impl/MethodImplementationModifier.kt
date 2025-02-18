package com.huanli233.dex_mixin.core.utils.dexlib2.mutable_impl

import com.android.tools.smali.dexlib2.Format.*
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.builder.BuilderInstruction
import com.android.tools.smali.dexlib2.builder.MutableMethodImplementation
import com.android.tools.smali.dexlib2.builder.instruction.*
import com.android.tools.smali.dexlib2.iface.MethodImplementation
import com.android.tools.smali.dexlib2.iface.Method
import com.huanli233.dex_mixin.core.utils.dexlib2.SmaliType
import com.huanli233.dex_mixin.core.utils.dexlib2.mutable_impl.RegisterManager.getRegisters
import com.huanli233.dex_mixin.core.utils.dexlib2.settable

class MethodImplementationModifier(
    private val srcMethod: Method,
    private val originalImplementation: MethodImplementation
): MutableMethodImplementation(originalImplementation) {

    var mutableRegisterCount = originalImplementation.registerCount

    private var fixParamRegisters = false
    private val originalRegisterCount = mutableRegisterCount

    override fun getRegisterCount(): Int {
        return registerCount
    }

    fun insertAllInstructions(
        index: Int,
        instructions: Collection<BuilderInstruction>
    ) {
        instructions.forEachIndexed { i, instruction ->
            addInstruction(index + i, instruction)
        }
    }

    fun removeAllInstructions(
        index: Int,
        count: Int
    ) {
        repeat(count) {
            removeInstruction(index)
        }
    }

    fun removeAllInstructions(
        range: IntRange
    ) {
        repeat(range.count()) {
            removeInstruction(range.start)
        }
    }

    fun getExtraRegister(
        count: Int
    ): Set<Int> {
        val registers = getRegisters(
            method = srcMethod,
            registerCountSettable = mutableRegisterCount.settable { i -> mutableRegisterCount = i },
            count = count
        )
        fixParamRegisters = true
        return registers
    }

    override fun getInstructions(): List<BuilderInstruction?> {
        var instructions = super.instructions
        if (fixParamRegisters) {
            instructions.forEachIndexed { index, instruction ->
                // TODO Handle register out of range
                instruction.run {
                    when (format) {
                        Format11x -> {
                            (this as BuilderInstruction11x).let { x ->
                                val (fixedRegisters, containsParamRegister) = fixInstructionParamRegisters(registerA)
                                if (containsParamRegister) {
                                    BuilderInstruction11x(
                                        opcode,
                                        fixedRegisters[0]
                                    )
                                } else null
                            }
                        }
                        Format12x -> {
                            (this as BuilderInstruction12x).let { x ->
                                val (fixedRegisters, containsParamRegister) = fixInstructionParamRegisters(registerA, registerB)
                                if (containsParamRegister) {
                                    BuilderInstruction12x(
                                        opcode,
                                        fixedRegisters[0],
                                        fixedRegisters[1]
                                    )
                                } else null
                            }
                        }
                        Format22x -> {
                            (this as BuilderInstruction22x).let { x ->
                                val (fixedRegisters, containsParamRegister) = fixInstructionParamRegisters(registerA, registerB)
                                if (containsParamRegister) {
                                    BuilderInstruction22x(
                                        opcode,
                                        fixedRegisters[0],
                                        fixedRegisters[1]
                                    )
                                } else null
                            }
                        }
                        Format32x -> {
                            (this as BuilderInstruction32x).let { x ->
                                val (fixedRegisters, containsParamRegister) = fixInstructionParamRegisters(registerA, registerB)
                                if (containsParamRegister) {
                                    BuilderInstruction32x(
                                        opcode,
                                        fixedRegisters[0],
                                        fixedRegisters[1]
                                    )
                                } else null
                            }
                        }
                        Format11n -> {
                            (this as BuilderInstruction11n).let { x ->
                                val (fixedRegisters, containsParamRegister) = fixInstructionParamRegisters(registerA)
                                if (containsParamRegister) {
                                    BuilderInstruction11n(
                                        opcode,
                                        fixedRegisters[0],
                                        narrowLiteral
                                    )
                                } else null
                            }
                        }
                        Format21s -> {
                            (this as BuilderInstruction21s).let { x ->
                                val (fixedRegisters, containsParamRegister) = fixInstructionParamRegisters(registerA)
                                if (containsParamRegister) {
                                    BuilderInstruction21s(
                                        opcode,
                                        fixedRegisters[0],
                                        narrowLiteral
                                    )
                                } else null
                            }
                        }
                        Format31i -> {
                            (this as BuilderInstruction31i).let { x ->
                                val (fixedRegisters, containsParamRegister) = fixInstructionParamRegisters(registerA)
                                if (containsParamRegister) {
                                    BuilderInstruction31i(
                                        opcode,
                                        fixedRegisters[0],
                                        narrowLiteral
                                    )
                                } else null
                            }
                        }
                        Format21ih -> {
                            (this as BuilderInstruction21ih).let { x ->
                                val (fixedRegisters, containsParamRegister) = fixInstructionParamRegisters(registerA)
                                if (containsParamRegister) {
                                    BuilderInstruction32x(
                                        opcode,
                                        fixedRegisters[0],
                                        narrowLiteral
                                    )
                                } else null
                            }
                        }
                        Format51l -> {
                            (this as BuilderInstruction51l).let { x ->
                                val (fixedRegisters, containsParamRegister) = fixInstructionParamRegisters(registerA)
                                if (containsParamRegister) {
                                    BuilderInstruction51l(
                                        opcode,
                                        fixedRegisters[0],
                                        wideLiteral
                                    )
                                } else null
                            }
                        }
                        Format21lh -> {
                            (this as BuilderInstruction21lh).let { x ->
                                val (fixedRegisters, containsParamRegister) = fixInstructionParamRegisters(registerA)
                                if (containsParamRegister) {
                                    BuilderInstruction21lh(
                                        opcode,
                                        fixedRegisters[0],
                                        wideLiteral
                                    )
                                } else null
                            }
                        }
                        Format21c -> {
                            (this as BuilderInstruction21c).let { x ->
                                val (fixedRegisters, containsParamRegister) = fixInstructionParamRegisters(registerA)
                                if (containsParamRegister) {
                                    BuilderInstruction21c(
                                        opcode,
                                        fixedRegisters[0],
                                        reference
                                    )
                                } else null
                            }
                        }
                        Format31c -> {
                            (this as BuilderInstruction31c).let { x ->
                                val (fixedRegisters, containsParamRegister) = fixInstructionParamRegisters(registerA)
                                if (containsParamRegister) {
                                    BuilderInstruction31c(
                                        opcode,
                                        fixedRegisters[0],
                                        reference
                                    )
                                } else null
                            }
                        }
                        Format35c -> {
                            (this as BuilderInstruction35c).let { x ->
                                val (fixedRegisters, containsParamRegister) = fixInstructionParamRegisters(registerC, registerD, registerE, registerF, registerG)
                                if (containsParamRegister) {
                                    BuilderInstruction35c(
                                        opcode,
                                        registerCount,
                                        fixedRegisters[0],
                                        fixedRegisters[1],
                                        fixedRegisters[2],
                                        fixedRegisters[3],
                                        fixedRegisters[4],
                                        reference
                                    )
                                } else null
                            }
                        }
                        Format22c -> {
                            (this as BuilderInstruction22c).let { x ->
                                val (fixedRegisters, containsParamRegister) = fixInstructionParamRegisters(registerA, registerB)
                                if (containsParamRegister) {
                                    BuilderInstruction22c(
                                        opcode,
                                        fixedRegisters[0],
                                        fixedRegisters[1],
                                        reference
                                    )
                                } else null
                            }
                        }
                        Format31t -> {
                            (this as BuilderInstruction31t).let { x ->
                                val (fixedRegisters, containsParamRegister) = fixInstructionParamRegisters(registerA)
                                if (containsParamRegister) {
                                    BuilderInstruction31t(
                                        opcode,
                                        fixedRegisters[0],
                                        target
                                    )
                                } else null
                            }
                        }
                        Format3rc -> {
                            (this as BuilderInstruction3rc).let { x ->
                                val (fixedRegisters, containsParamRegister) = fixInstructionParamRegisters(startRegister)
                                if (containsParamRegister) {
                                    BuilderInstruction3rc(
                                        opcode,
                                        fixedRegisters[0],
                                        registerCount,
                                        reference
                                    )
                                } else null
                            }
                        }
                        Format23x -> {
                            (this as BuilderInstruction23x).let { x ->
                                val (fixedRegisters, containsParamRegister) = fixInstructionParamRegisters(registerA, registerB, registerC)
                                if (containsParamRegister) {
                                    BuilderInstruction23x(
                                        opcode,
                                        fixedRegisters[0],
                                        fixedRegisters[1],
                                        fixedRegisters[2]
                                    )
                                } else null
                            }
                        }
                        Format22t -> {
                            (this as BuilderInstruction22t).let { x ->
                                val (fixedRegisters, containsParamRegister) = fixInstructionParamRegisters(registerA, registerB)
                                if (containsParamRegister) {
                                    BuilderInstruction22t(
                                        opcode,
                                        fixedRegisters[0],
                                        fixedRegisters[1],
                                        target
                                    )
                                } else null
                            }
                        }
                        Format21t -> {
                            (this as BuilderInstruction21t).let { x ->
                                val (fixedRegisters, containsParamRegister) = fixInstructionParamRegisters(registerA)
                                if (containsParamRegister) {
                                    BuilderInstruction21t(
                                        opcode,
                                        fixedRegisters[0],
                                        target
                                    )
                                } else null
                            }
                        }
                        Format22s -> {
                            (this as BuilderInstruction22s).let { x ->
                                val (fixedRegisters, containsParamRegister) = fixInstructionParamRegisters(
                                    registerA,
                                    registerB
                                )
                                if (containsParamRegister) {
                                    BuilderInstruction22s(
                                        opcode,
                                        fixedRegisters[0],
                                        fixedRegisters[1],
                                        narrowLiteral
                                    )
                                } else null
                            }
                        }
                        Format22b -> {
                            (this as BuilderInstruction22b).let { x ->
                                val (fixedRegisters, containsParamRegister) = fixInstructionParamRegisters(
                                    registerA,
                                    registerB
                                )
                                if (containsParamRegister) {
                                    BuilderInstruction22b(
                                        opcode,
                                        fixedRegisters[0],
                                        fixedRegisters[1],
                                        narrowLiteral
                                    )
                                } else null
                            }
                        }
                        else -> null
                    }
                }?.let { modified ->
                    replaceInstruction(index, modified)
                }
            }
        }
        return instructions
    }

    private fun fixInstructionParamRegisters(vararg registers: Int): Pair<List<Int>, Boolean> {
        val paramCount = srcMethod.parameters.size
        val originalParamRegisters = (originalRegisterCount - paramCount + 1..originalRegisterCount).toList()
        var containsParamRegister = false
        return registers.map { i ->
            val paramRegister = originalParamRegisters.indexOf(i)
            if (paramRegister != -1) {
                containsParamRegister = true
                (mutableRegisterCount - paramCount + 1..mutableRegisterCount).toList()[paramRegister]
            } else i
        } to containsParamRegister
    }

    fun useBigRegister(
        index: Int,
        registers: IntArray,
        dataType: SmaliType,
        block: (IntArray) -> Collection<BuilderInstruction>
    ) {
        val methodImpl = this

        val k = dataType.registerCount

        val totalSmallRegs = registers.size * k
        val smallRegArray = IntArray(registers.size) { it * k }

        val tempRegs = RegisterManager.getTempRegisters(methodImpl, totalSmallRegs)

        val saveInstructions = mutableListOf<BuilderInstruction>()
        val moveToSmallInstructions = mutableListOf<BuilderInstruction>()
        val restoreInstructions = mutableListOf<BuilderInstruction>()

        for (i in registers.indices) {
            val smallStart = smallRegArray[i]
            val tempStart = tempRegs[i * k]
            for (offset in 0 until k) {
                val smallReg = smallStart + offset
                val tempReg = tempStart + offset
                saveInstructions += createMoveInstruction(tempReg, smallReg, dataType)
            }
        }

        // 生成移动大寄存器到小寄存器的指令
        for (i in registers.indices) {
            val sourceReg = registers[i]
            val smallStart = smallRegArray[i]
            for (offset in 0 until k) {
                val smallReg = smallStart + offset
                val sourceRegPart = sourceReg + offset
                moveToSmallInstructions += createMoveInstruction(smallReg, sourceRegPart, dataType)
            }
        }

        // 用户提供的指令
        val blockInstructions = block(smallRegArray)

        // 生成恢复原小寄存器的指令
        for (i in registers.indices) {
            val smallStart = smallRegArray[i]
            val tempStart = tempRegs[i * k]
            for (offset in 0 until k) {
                val smallReg = smallStart + offset
                val tempReg = tempStart + offset
                restoreInstructions += createMoveInstruction(smallReg, tempReg, dataType)
            }
        }

        // 合并所有指令并插入到指定位置
        val allInstructions = saveInstructions + moveToSmallInstructions + blockInstructions + restoreInstructions
        methodImpl.insertAllInstructions(index, allInstructions)
    }

    private fun createMoveInstruction(dest: Int, src: Int, dataType: SmaliType): BuilderInstruction {
        val opcode = when {
            src <= 0xff -> when (dataType) {
                SmaliType.INT -> Opcode.MOVE
                SmaliType.LONG -> Opcode.MOVE_WIDE
                SmaliType.FLOAT -> Opcode.MOVE
                SmaliType.DOUBLE -> Opcode.MOVE_WIDE
                SmaliType.OBJECT -> Opcode.MOVE_OBJECT
                else -> throw IllegalArgumentException("Unsupported data type")
            }
            src <= 0xffff -> when (dataType) {
                SmaliType.INT -> Opcode.MOVE_FROM16
                SmaliType.LONG -> Opcode.MOVE_WIDE_FROM16
                SmaliType.FLOAT -> Opcode.MOVE_FROM16
                SmaliType.DOUBLE -> Opcode.MOVE_WIDE_FROM16
                SmaliType.OBJECT -> Opcode.MOVE_OBJECT_FROM16
                else -> throw IllegalArgumentException("Unsupported data type")
            }
            else -> when (dataType) {
                SmaliType.INT -> Opcode.MOVE_16
                SmaliType.LONG -> Opcode.MOVE_WIDE_16
                SmaliType.FLOAT -> Opcode.MOVE_16
                SmaliType.DOUBLE -> Opcode.MOVE_WIDE_16
                SmaliType.OBJECT -> Opcode.MOVE_OBJECT_16
                else -> throw IllegalArgumentException("Unsupported data type")
            }
        }

        return when (opcode.format) {
            Format12x -> BuilderInstruction12x(opcode, dest, src)
            Format22x -> BuilderInstruction22x(opcode, dest, src)
            Format32x -> BuilderInstruction32x(opcode, dest, src)
            else -> throw IllegalArgumentException("Unsupported opcode format: ${opcode.format}")
        }
    }

    // DataType扩展属性，获取寄存器数目
    val SmaliType.registerCount: Int
        get() = when (this) {
            SmaliType.LONG, SmaliType.DOUBLE -> 2
            else -> 1
        }
}

fun MethodImplementation.modify(
    srcMethod: Method,
    block: MethodImplementationModifier.() -> Unit
): MethodImplementation = MethodImplementationModifier(srcMethod, this).apply(block)

fun Method.modifyImplementation(block: MethodImplementationModifier.() -> Unit)
    = implementation?.modify(this, block) ?: throw IllegalStateException("Method has no implementation")