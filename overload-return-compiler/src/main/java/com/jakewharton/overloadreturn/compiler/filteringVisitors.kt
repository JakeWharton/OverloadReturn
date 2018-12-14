package com.jakewharton.overloadreturn.compiler

import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes.ASM7

internal class FilteringClassVisitor(delegate: ClassVisitor) : ClassVisitor(ASM7, delegate) {
  override fun visitMethod(access: Int, name: String, descriptor: String, signature: String?,
      exceptions: Array<out String>?): MethodVisitor {
    return FilteringMethodVisitor(
        super.visitMethod(access, name, descriptor, signature, exceptions))
  }
}

internal class FilteringMethodVisitor(delegate: MethodVisitor) : MethodVisitor(ASM7, delegate) {
  override fun visitAnnotation(descriptor: String, visible: Boolean): AnnotationVisitor? {
    if (descriptor == overloadReturnDescriptor) {
      return null
    }
    return super.visitAnnotation(descriptor, visible)
  }
}
