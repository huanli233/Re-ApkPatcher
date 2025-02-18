package com.huanli233.dex_mixin.core.utils.dexlib2

import com.android.tools.smali.dexlib2.iface.Annotation
import com.android.tools.smali.dexlib2.iface.ClassDef
import com.android.tools.smali.dexlib2.iface.Field
import com.android.tools.smali.dexlib2.iface.Method
import com.android.tools.smali.dexlib2.rewriter.ClassDefRewriter
import com.android.tools.smali.dexlib2.rewriter.Rewriters
import com.huanli233.dex_mixin.core.utils.dexlib2.RewriterManager.ClassRewriterModule
import com.huanli233.dex_mixin.core.utils.dexlib2.RewriterManager.classesRewriters

object RewriterManager {

    val classesRewriters = mutableMapOf<ClassDef, MutableList<(Rewriters, ClassDef) -> ClassDef>>()
    val addedClasses = mutableSetOf<ClassDef>()

    class ClassRewriterModule(
        rewriters: Rewriters,
        val classDef: ClassDef
    ): ClassDefRewriter(rewriters) {

        var type: String = classDef.type
        var accessFlags: Int = classDef.accessFlags
        var superClass: String? = classDef.superclass
        var interfaces: MutableList<String> = classDef.interfaces
        var sourceFile: String? = classDef.sourceFile
        var annotations: MutableSet<Annotation> = classDef.annotations as MutableSet<Annotation>
        var staticFields: Set<Field> = classDef.staticFields.toSet()
        var instanceFields: Set<Field> = classDef.instanceFields.toSet()
        var fields: MutableSet<Field> = classDef.fields.toMutableSet()
        var directMethods: Set<Method> = classDef.directMethods.toSet()
        var virtualMethods: Set<Method> = classDef.virtualMethods.toSet()
        var methods: MutableSet<Method> = classDef.methods.toMutableSet()

        fun toClassDef(): ClassDef {
            return super.rewrite(object : RewrittenClassDef(classDef) {
                override fun getType(): String {
                    return this@ClassRewriterModule.type
                }
                override fun getAccessFlags(): Int {
                    return this@ClassRewriterModule.accessFlags
                }
                override fun getSuperclass(): String? {
                    return this@ClassRewriterModule.superClass
                }
                override fun getInterfaces(): List<String> {
                    return this@ClassRewriterModule.interfaces
                }
                override fun getSourceFile(): String? {
                    return this@ClassRewriterModule.sourceFile
                }
                override fun getAnnotations(): Set<Annotation> {
                    return this@ClassRewriterModule.annotations
                }
                override fun getStaticFields(): Iterable<Field> {
                    return this@ClassRewriterModule.staticFields
                }
                override fun getInstanceFields(): Iterable<Field> {
                    return this@ClassRewriterModule.instanceFields
                }
                override fun getFields(): Iterable<Field> {
                    return this@ClassRewriterModule.fields
                }
                override fun getDirectMethods(): Iterable<Method> {
                    return this@ClassRewriterModule.directMethods
                }
                override fun getVirtualMethods(): Iterable<Method> {
                    return this@ClassRewriterModule.virtualMethods
                }
                override fun getMethods(): Iterable<Method> {
                    return this@ClassRewriterModule.methods
                }
            })
        }

    }

}

fun ClassDef.rewrite(rewriter: ClassRewriterModule.() -> Unit) {
    val existingRewriter = classesRewriters[this] ?: mutableListOf()
    existingRewriter.add { rewriters, classDef ->
        ClassRewriterModule(rewriters, classDef).apply(rewriter).toClassDef()
    }
}