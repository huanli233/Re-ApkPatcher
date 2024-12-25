package com.huanli233.dex_mixin.core.dex

import com.android.tools.smali.dexlib2.iface.ClassDef
import com.android.tools.smali.dexlib2.iface.DexFile
import com.android.tools.smali.dexlib2.rewriter.*
import com.huanli233.dex_mixin.api.annotations.Mixin
import com.huanli233.dex_mixin.api.utils.smaliType
import com.huanli233.dex_mixin.core.exceptions.DexMixinException
import com.huanli233.dex_mixin.core.utils.dexlib2.RewriterManager
import com.huanli233.dex_mixin.core.utils.getEntryInputStream
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
        }).dexFileRewriter.rewrite(targetAppDex)
    }

    private fun applyMixin() {
        mixinClasses.forEach { mixinClass ->
            mixinClass.annotations.find { it.type == Mixin::class.smaliType }?.elements?.let { elements ->
                val targets = TargetFinder.findTargetByMixinAnnotation(
                    elements,
                    targetAppDex.classes
                )
                TODO("not implemented")
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