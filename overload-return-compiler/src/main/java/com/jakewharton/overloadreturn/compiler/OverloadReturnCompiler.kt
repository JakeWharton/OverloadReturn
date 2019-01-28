package com.jakewharton.overloadreturn.compiler

import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes.ACC_BRIDGE
import org.objectweb.asm.Opcodes.ACC_STATIC
import org.objectweb.asm.Opcodes.ACC_SYNTHETIC
import org.objectweb.asm.Opcodes.ALOAD
import org.objectweb.asm.Opcodes.DLOAD
import org.objectweb.asm.Opcodes.INVOKESTATIC
import org.objectweb.asm.Opcodes.INVOKEVIRTUAL
import org.objectweb.asm.Opcodes.LLOAD
import org.objectweb.asm.Opcodes.POP
import org.objectweb.asm.Type
import org.objectweb.asm.Type.VOID_TYPE

class OverloadReturnCompiler(
  val debug: Boolean = false
) {
  fun parse(bytes: ByteArray) : ClassInfo {
    val overloads = mutableListOf<ReturnOverload>()
    ClassReader(bytes).accept(ParsingClassVisitor(overloads, debug), 0)

    return ClassInfo(bytes, overloads)
  }
}

@Suppress("ArrayInDataClass")
data class ClassInfo(
  val originalBytes: ByteArray,
  val overloads: List<ReturnOverload>
) {
  fun toBytes(): ByteArray {
    if (overloads.isEmpty()) {
      return originalBytes
    }

    val writer = ClassWriter(null, 0)
    val filteringVisitor = FilteringClassVisitor(writer)
    ClassReader(originalBytes).accept(filteringVisitor, 0)

    overloads.forEach { target ->
      val argumentTypes = Type.getArgumentTypes(target.descriptor)
      val newDescriptor = Type.getMethodDescriptor(target.returnOverload, *argumentTypes)

      writer.visitMethod(target.access.withFlags(ACC_BRIDGE, ACC_SYNTHETIC), target.name,
          newDescriptor, target.signature, target.exceptions).apply {
        visitCode()

        var localIndex = 0

        if (ACC_STATIC isNotFlagIn target.access) {
          visitVarInsn(ALOAD, localIndex++)
        }
        for (argumentType in argumentTypes) {
          val instruction = argumentType.toVarInstruction()
          visitVarInsn(instruction, localIndex)

          localIndex += when (instruction) {
            DLOAD, LLOAD -> 2
            else -> 1
          }
        }

        val invoke = if (ACC_STATIC isFlagIn target.access) INVOKESTATIC else INVOKEVIRTUAL
        visitMethodInsn(invoke, target.owner, target.name, target.descriptor, false)

        if (target.returnOverload == VOID_TYPE) {
          visitInsn(POP)
        }
        visitInsn(target.returnOverload.toReturnInstruction())

        // Since void is never the return type of the annotated target method, we always need at
        // least a stack size of 1.
        val stackSize = maxOf(1, localIndex)
        visitMaxs(stackSize, localIndex)

        visitEnd()
      }
    }

    return writer.toByteArray()
  }
}

@Suppress("ArrayInDataClass")
data class ReturnOverload(
  val owner: String,
  val access: Int,
  val name: String,
  val descriptor: String,
  val signature: String?,
  val exceptions: Array<out String>?,
  val returnOverload: Type
)
