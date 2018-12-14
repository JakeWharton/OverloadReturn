package com.jakewharton.overloadreturn.compiler

import com.google.testing.compile.Compiler
import com.jakewharton.overloadreturn.OverloadReturn
import org.objectweb.asm.ClassReader
import org.objectweb.asm.util.TraceClassVisitor
import java.io.PrintWriter
import java.io.StringWriter
import javax.tools.JavaFileObject

fun compile(vararg input: JavaFileObject): List<JavaFileObject> {
  return Compiler.javac()
      .withClasspathFrom(OverloadReturn::class.java.classLoader)
      .compile(*input)
      .generatedFiles()
}

fun JavaFileObject.readBytes() = openInputStream().use { it.readBytes() }

fun ByteArray.toBytecodeString(): String {
  val reader = ClassReader(this)
  val writer = StringWriter()
  reader.accept(TraceClassVisitor(PrintWriter(writer)), 0)
  return writer.toString().trimEnd()
}
