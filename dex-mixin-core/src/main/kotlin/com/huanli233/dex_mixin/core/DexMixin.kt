package com.huanli233.dex_mixin.core

import com.huanli233.dex_mixin.core.dex.DexProcessor
import com.huanli233.dex_mixin.core.exceptions.DexMixinException
import com.huanli233.dex_mixin.core.utils.exception
import lanchon.multidexlib2.BasicDexFileNamer
import lanchon.multidexlib2.DexFileNamer
import lanchon.multidexlib2.DexIO
import lanchon.multidexlib2.MultiDexIO
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

class DexMixin(
    val mixinAppFile: File,
    val d8Path: String = ""
) {

    val zipFile = ZipFile(mixinAppFile)

    fun mixin(
        targetApk: File
    ) {
        val namer = BasicDexFileNamer()
        val targetZipFile = ZipFile(targetApk)
        val dexCount = zipFile.getDexCount(namer)
        var mixinAppFileToRead = mixinAppFile
        if (dexCount == 0 && d8Path.isNotEmpty()) {
            val toDex = File(mixinAppFile.absolutePath + "_dex.zip").apply {
                if (exists()) delete()
            }
            ProcessBuilder(
                d8Path,
                "--output",
                toDex.absolutePath,
                mixinAppFile.absolutePath
            ).start().waitFor()
            if (toDex.exists() && ZipFile(toDex).getDexCount(namer) != 0) {
                mixinAppFileToRead = toDex
            } else {
                exception("d8 compile failed")
            }
        } else {
            exception("No dex file found in mixin app apk")
        }
        val mixinAppDex = runCatching {
            MultiDexIO.readDexFile(true, mixinAppFileToRead, namer, null, null)
        }.getOrElse { exception ->
            exception("Read mixin app apk failed", exception)
        }
        val targetDex = runCatching {
            MultiDexIO.readDexFile(true, targetApk, namer, null, null)
        }.getOrElse { exception ->
            exception("Read target apk failed", exception)
        }
        val rewrote = DexProcessor(zipFile, mixinAppDex, targetDex).rewriteTarget()
        val newDexOutputDir = File(targetApk.absoluteFile.parentFile, "newDex").apply {
            if (!exists()) mkdirs()
        }
        val targetDexCount = targetZipFile.getDexCount(namer)
        MultiDexIO.writeDexFile(true, targetDexCount, newDexOutputDir, namer, rewrote, DexIO.DEFAULT_MAX_DEX_POOL_SIZE, null)
        addOrReplaceFilesInZip(
            zipFile = targetApk,
            newFile = File(targetApk.absoluteFile.parentFile, targetApk.nameWithoutExtension + "_patched" + targetApk.extension),
            newFiles = newDexOutputDir.listFiles()?.associateBy { it.name } ?: emptyMap()
        )
        TODO("Resource injection is not implemented")
    }

    private fun ZipFile.getDexCount(namer: DexFileNamer): Int {
        return generateSequence(0) { it + 1 }
            .takeWhile { getEntry(namer.getName(it)) != null }
            .count()
    }

    private fun addOrReplaceFilesInZip(
        zipFile: File,
        newFile: File,
        newFiles: Map<String, File>
    ) {
        ZipOutputStream(FileOutputStream(newFile)).use { zipOut ->
            if (zipFile.exists()) {
                ZipInputStream(FileInputStream(zipFile)).use { zipIn ->
                    var entry: ZipEntry?
                    while (zipIn.nextEntry.also { entry = it } != null) {
                        val entryName = entry?.name!!
                        if (newFiles.containsKey(entryName)) {
                            val newFile = newFiles[entryName]!!
                            zipOut.putNextEntry(ZipEntry(entryName))
                            newFile.inputStream().copyTo(zipOut)
                        } else {
                            zipOut.putNextEntry(entry)
                            zipIn.copyTo(zipOut)
                        }
                        zipOut.closeEntry()
                        zipIn.closeEntry()
                    }
                }
            }

            for ((entryName, newFile) in newFiles) {
                val entry = ZipEntry(entryName)
                zipOut.putNextEntry(entry)
                newFile.inputStream().copyTo(zipOut)
                zipOut.closeEntry()
            }
        }
    }

}