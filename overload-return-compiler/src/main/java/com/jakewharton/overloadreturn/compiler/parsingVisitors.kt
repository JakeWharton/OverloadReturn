package com.jakewharton.overloadreturn.compiler

import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type

internal class OverloadReturnParsingClassVisitor(
    private val overloads: MutableList<ReturnOverload>,
    private val debug: Boolean = false
) : ClassVisitor(Opcodes.ASM7) {

  private lateinit var owner: String

  override fun visit(version: Int, access: Int, name: String, signature: String?,
      superName: String?, interfaces: Array<out String>?) {
    if (debug) {
      println("OWNER: $name")
    }
    owner = name
  }

  override fun visitMethod(access: Int, name: String, descriptor: String, signature: String?,
      exceptions: Array<out String>?): MethodVisitor {
    if (debug) {
      println("visitMethod[access: $access, name: $name, descriptor: $descriptor, signature: $signature, exceptions: ${exceptions?.contentToString()}]")
    }
    return OverloadReturnParsingMethodVisitor(owner, access, name, descriptor, signature, exceptions,
        overloads, debug)
  }
}

internal class OverloadReturnParsingMethodVisitor(
    private val owner: String,
    private val access: Int,
    private val name: String,
    private val descriptor: String,
    private val signature: String?,
    private val exceptions: Array<out String>?,
    private val targets: MutableList<ReturnOverload>,
    private val debug: Boolean = false
) : MethodVisitor(Opcodes.ASM7) {
  override fun visitAnnotation(descriptor: String, visible: Boolean): AnnotationVisitor? {
    if (debug) {
      println("visitAnnotation[descriptor: $descriptor, visible: $visible]")
    }
    if (descriptor == overloadReturnDescriptor) {
      return OverloadReturnParsingAnnotationVisitor(owner, access, name, this.descriptor, signature,
          exceptions, targets, debug)
    }
    return null
  }
}

internal class OverloadReturnParsingAnnotationVisitor(
    private val owner: String,
    private val access: Int,
    private val name: String,
    private val descriptor: String,
    private val signature: String?,
    private val exceptions: Array<out String>?,
    private val targets: MutableList<ReturnOverload>,
    private val debug: Boolean = false
) : AnnotationVisitor(Opcodes.ASM7) {
  override fun visit(name: String?, value: Any) {
    require(value is Type)
    if (debug) {
      println("XXX $name $value ${value::class.java}")
    }
    targets.add(ReturnOverload(owner, access, this.name, descriptor, signature, exceptions, value))
  }

  override fun visitArray(name: String): AnnotationVisitor {
    require(name == "value")
    return this
  }
}
