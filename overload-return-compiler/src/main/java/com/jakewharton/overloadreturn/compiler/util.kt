package com.jakewharton.overloadreturn.compiler

import com.jakewharton.overloadreturn.OverloadReturn
import org.objectweb.asm.Opcodes.ALOAD
import org.objectweb.asm.Opcodes.ARETURN
import org.objectweb.asm.Opcodes.DLOAD
import org.objectweb.asm.Opcodes.DRETURN
import org.objectweb.asm.Opcodes.FLOAD
import org.objectweb.asm.Opcodes.FRETURN
import org.objectweb.asm.Opcodes.ILOAD
import org.objectweb.asm.Opcodes.IRETURN
import org.objectweb.asm.Opcodes.LLOAD
import org.objectweb.asm.Opcodes.LRETURN
import org.objectweb.asm.Opcodes.RETURN
import org.objectweb.asm.Type
import org.objectweb.asm.Type.BOOLEAN_TYPE
import org.objectweb.asm.Type.BYTE_TYPE
import org.objectweb.asm.Type.CHAR_TYPE
import org.objectweb.asm.Type.DOUBLE_TYPE
import org.objectweb.asm.Type.FLOAT_TYPE
import org.objectweb.asm.Type.INT_TYPE
import org.objectweb.asm.Type.LONG_TYPE
import org.objectweb.asm.Type.SHORT_TYPE
import org.objectweb.asm.Type.VOID_TYPE

internal val overloadReturnDescriptor = Type.getDescriptor(OverloadReturn::class.java)

internal fun Type.toVarInstruction() = when (this) {
  BOOLEAN_TYPE, BYTE_TYPE, CHAR_TYPE, INT_TYPE, SHORT_TYPE -> ILOAD
  DOUBLE_TYPE -> DLOAD
  FLOAT_TYPE -> FLOAD
  LONG_TYPE -> LLOAD
  else -> ALOAD
}

internal fun Type.toReturnInstruction() = when (this) {
  BOOLEAN_TYPE, BYTE_TYPE, CHAR_TYPE, INT_TYPE, SHORT_TYPE -> IRETURN
  DOUBLE_TYPE -> DRETURN
  FLOAT_TYPE -> FRETURN
  LONG_TYPE -> LRETURN
  VOID_TYPE -> RETURN
  else -> ARETURN
}

internal infix fun Int.isFlagIn(value: Int) = (value and this) != 0
internal infix fun Int.isNotFlagIn(value: Int) = (value and this) == 0

internal fun Int.withFlags(flag1: Int, flag2: Int) = this or flag1 or flag2
