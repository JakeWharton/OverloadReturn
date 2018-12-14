@file:JvmName("Main")

package com.jakewharton.overloadreturn.compiler

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file

fun main(vararg args: String) {
  OverloadReturnCommand().main(args.toList())
}

private class OverloadReturnCommand : CliktCommand() {
  private val debug by option().flag()
  private val inputFile by argument().file()

  override fun run() {
    val inputBytes = inputFile.readBytes()
    val outputBytes = OverloadReturnCompiler(debug).parse(inputBytes).toBytes()

    val outputFile = inputFile.resolveSibling("NEW_" + inputFile.name)
    outputFile.writeBytes(outputBytes)
  }
}
