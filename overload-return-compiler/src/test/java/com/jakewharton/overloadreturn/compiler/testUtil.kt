package com.jakewharton.overloadreturn.compiler

import com.google.testing.compile.Compiler
import com.jakewharton.overloadreturn.OverloadReturn
import org.objectweb.asm.ClassReader
import org.objectweb.asm.util.TraceClassVisitor
import java.io.PrintWriter
import java.io.StringWriter
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.LinkOption
import java.nio.file.OpenOption
import java.nio.file.Path
import java.nio.file.attribute.FileAttribute
import javax.tools.JavaFileObject
import kotlin.text.Charsets.UTF_8

fun compile(vararg input: JavaFileObject): List<JavaFileObject> {
  return Compiler.javac()
      .withClasspathFrom(OverloadReturn::class.java.classLoader)
      .compile(*input)
      .generatedFiles()
}

fun JavaFileObject.toByteArray() = openInputStream().use { it.readBytes() }

fun ByteArray.toBytecodeString(): String {
  val reader = ClassReader(this)
  val writer = StringWriter()
  reader.accept(TraceClassVisitor(PrintWriter(writer)), 0)
  return writer.toString().trimEnd()
}

fun Path.createDirectory(vararg attrs: FileAttribute<*>) {
  Files.createDirectory(this, *attrs)
}
fun Path.createDirectories(vararg attrs: FileAttribute<*>) {
  Files.createDirectories(this, *attrs)
}
fun Path.writeBytes(bytes: ByteArray, vararg options: OpenOption) {
  Files.write(this, bytes, *options)
}
fun Path.writeText(text: String, charset: Charset = UTF_8, vararg options: OpenOption) {
  writeBytes(text.toByteArray(charset), *options)
}
fun Path.readBytes(): ByteArray {
  return Files.readAllBytes(this)
}
fun Path.readText(charset: Charset = UTF_8): String {
  return String(readBytes(), charset)
}
fun Path.exists(vararg options: LinkOption) = Files.exists(this, *options)
