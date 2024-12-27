package com.huanli233.dex_mixin.core.dex

import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.ClassDef
import com.android.tools.smali.dexlib2.iface.DexFile
import com.android.tools.smali.dexlib2.iface.instruction.Instruction
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction21c
import com.android.tools.smali.dexlib2.iface.reference.FieldReference
import com.android.tools.smali.dexlib2.iface.reference.MethodReference
import com.android.tools.smali.dexlib2.iface.reference.TypeReference
import com.android.tools.smali.dexlib2.immutable.ImmutableMethod
import com.android.tools.smali.dexlib2.immutable.instruction.ImmutableInstruction21c
import com.android.tools.smali.dexlib2.immutable.reference.ImmutableFieldReference
import com.android.tools.smali.dexlib2.immutable.reference.ImmutableMethodReference
import com.android.tools.smali.dexlib2.immutable.reference.ImmutableTypeReference
import com.android.tools.smali.dexlib2.rewriter.*
import com.huanli233.dex_mixin.api.annotations.Mixin
import com.huanli233.dex_mixin.api.annotations.Shadow
import com.huanli233.dex_mixin.api.utils.smaliType
import com.huanli233.dex_mixin.api.utils.smaliTypeToJava
import com.huanli233.dex_mixin.core.exceptions.DexMixinException
import com.huanli233.dex_mixin.core.utils.dexlib2.AnnotationValueParser
import com.huanli233.dex_mixin.core.utils.dexlib2.Converters.referenceType
import com.huanli233.dex_mixin.core.utils.dexlib2.ReferenceType
import com.huanli233.dex_mixin.core.utils.dexlib2.RewriterManager
import com.huanli233.dex_mixin.core.utils.dexlib2.copy
import com.huanli233.dex_mixin.core.utils.dexlib2.rewrite
import com.huanli233.dex_mixin.core.utils.dexlib2.signature
import com.huanli233.dex_mixin.core.utils.getEntryInputStream
import com.huanli233.dex_mixin.core.utils.kotlin.cast
import com.huanli233.dex_mixin.core.utils.withReader
import java.util.zip.ZipFile

private const val CLASSES_CONF_FILEPATH = "META-INF/dex-mixin/classes.conf"

class DexProcessor(
    val mixinAppZipFile: ZipFile,
    mixinDex: DexFile,
    val targetAppDex: DexFile
) {

    companion object {

        private fun matches(patterns: Set<String>, input: String): Boolean {
            patterns.forEach { pattern ->
                if (matchWithWildcard(pattern.split('.'), input.split('.'))) {
                    return true
                }
            }
            return false
        }

        private fun matchWithWildcard(patternParts: List<String>, inputParts: List<String>): Boolean {
            var i = 0
            var j = 0
            while (i < patternParts.size && j < inputParts.size) {
                val patternPart = patternParts[i]
                val inputPart = inputParts[j]

                when (patternPart) {
                    "**" -> {
                        if (i == patternParts.size - 1) {
                            return true
                        }
                        for (k in j..inputParts.size) {
                            if (matchWithWildcard(patternParts.subList(i + 1, patternParts.size), inputParts.subList(k, inputParts.size))) {
                                return true
                            }
                        }
                        return false
                    }
                    "*" -> {
                        i++
                        j++
                    }
                    inputPart -> {
                        i++
                        j++
                    }
                    else -> {
                        return false
                    }
                }
            }

            return i == patternParts.size && j == inputParts.size
        }

    }

    val normalClasses = mutableSetOf<ClassDef>()
    val mixinClasses = mutableSetOf<ClassDef>()

    val mixinTargetsMap = mutableMapOf<String, String>()

    init {
        mixinDex.classes.forEach { clazz ->
            if (clazz.annotations.find {
                    it.type == Mixin::class.smaliType
                } != null) {
                mixinClasses.add(clazz)
            } else {
                normalClasses.add(clazz)
            }
        }
    }

    fun rewriteTarget(): DexFile {
        addOrReplaceNormalClassesToTarget()
        applyMixin()
        return DexRewriter(object : RewriterModule() {
            override fun getClassDefRewriter(rewriters: Rewriters): Rewriter<ClassDef?> {
                return object : ClassDefRewriter(rewriters) {
                    override fun rewrite(classDef: ClassDef): ClassDef {
                        var rewroteClassDef = classDef
                        RewriterManager.classesRewriters[classDef]?.forEach { item ->
                            rewroteClassDef = item(rewriters, rewroteClassDef)
                        }
                        return rewroteClassDef
                    }
                }
            }

            override fun getTypeRewriter(rewriters: Rewriters): Rewriter<String?> {
                return object : TypeRewriter() {
                    override fun rewrite(value: String): String {
                        return super.rewrite(mixinTargetsMap[value] ?: value)
                    }
                }
            }

            override fun getFieldReferenceRewriter(rewriters: Rewriters): Rewriter<FieldReference?> {
                return object : FieldReferenceRewriter(rewriters) {
                    override fun rewrite(fieldReference: FieldReference): FieldReference {
                        return super.rewrite(fieldReference.run {
                            mixinTargetsMap[definingClass]?.let { targetType ->
                                ImmutableFieldReference(
                                    /* definingClass = */ targetType,
                                    /* name = */ name,
                                    /* type = */ type
                                )
                            } ?: fieldReference
                        })
                    }
                }
            }

            override fun getMethodReferenceRewriter(rewriters: Rewriters): Rewriter<MethodReference?> {
                return object : MethodReferenceRewriter(rewriters) {
                    override fun rewrite(methodReference: MethodReference): MethodReference {
                        return super.rewrite(methodReference.run {
                            mixinTargetsMap[definingClass]?.let { targetType ->
                                ImmutableMethodReference(
                                    /* definingClass = */ targetType,
                                    /* name = */ name,
                                    /* parameters = */ parameterTypes,
                                    /* returnType = */ returnType
                                )
                            } ?: methodReference
                        })
                    }
                }
            }
        }).dexFileRewriter.rewrite(targetAppDex)
    }

    private fun applyMixin() {
        mixinClasses.forEach { mixinClass ->
            mixinClass.annotations.find { it.type == Mixin::class.smaliType }?.elements?.let { elements ->
                val target = TargetFinder.findTargetByMixinAnnotation(
                    mixinClass.type,
                    AnnotationValueParser.parseMixin(elements),
                    targetAppDex.classes
                ) ?: throw DexMixinException("Can't find target class for mixin class ${mixinClass.type.smaliTypeToJava()}")
                mixinTargetsMap[mixinClass.type] = target.type
                target.rewrite {
                    mixinClass.fields.filter {
                        it.annotations.any { annotation -> annotation.type == Shadow::class.smaliType }.not()
                    }.forEach { field ->
                        if (fields.any { it.signature == field.signature }) {
                            throw DexMixinException("Field ${field.signature} already exists in target class ${target.type.smaliTypeToJava()}")
                        }
                        fields.add(field.copy(
                            definingClass = target.type
                        ))
                    }
                    mixinClass.methods.forEach { method ->
                        TODO("Not implemented")
                    }
                }
            }
        }
    }

    private fun addOrReplaceNormalClassesToTarget() {
        val targetClassTypes = targetAppDex.classes.map {
            it.type
        }
        val (ignoreClassesSet, replaceClassesSet) = readClassesConf()
        normalClasses.forEach { classDef ->
            if (targetClassTypes.contains(classDef.type)) {
                if (matches(ignoreClassesSet, classDef.type)) {
                    return@forEach
                } else if (!matches(replaceClassesSet, classDef.type)) {
                    throw DexMixinException("The class definition ${classDef.type} already exists in the target application class, but it is not configured to be ignored or replaced")
                }
            }
            (targetAppDex.classes as MutableSet<ClassDef>).add(classDef)
        }
    }

    private fun readClassesConf(): Pair<Set<String>, Set<String>> {
        val ignoreClassesSet = mutableSetOf<String>()
        val replaceClassesSet = mutableSetOf<String>()

        mixinAppZipFile.getEntryInputStream(CLASSES_CONF_FILEPATH)?.withReader { reader ->
            var count = 1
            reader.forEachLine { line ->
                val split = line.trim().split(" ")
                if (split.size < 2) {
                    throw DexMixinException("Content of $CLASSES_CONF_FILEPATH is illegal, at line #$count")
                } else {
                    val command = split[0]
                    val content = line.trim().substring(command.length)
                    when (command) {
                        "ignore" -> ignoreClassesSet.add(content)
                        "replace" -> replaceClassesSet.add(content)
                    }
                }
                count++
            }
        }

        return (ignoreClassesSet to replaceClassesSet)
    }

}