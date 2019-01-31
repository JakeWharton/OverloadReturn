@file:JvmName("Main")

package com.jakewharton.overloadreturn.compiler

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.path

fun main(vararg args: String) {
  OverloadReturnCommand().main(args.toList())
}

private class OverloadReturnCommand : CliktCommand(name = "overload-return-compiler") {
  private val debug by option(help = "Show debug information while processing").flag()
  private val input by argument(help = "Directory containing class files and resources").path()
  private val output by argument(help = "Directory for processed class files and resources").path()

  override fun run() {
    OverloadReturnCompiler(debug).processDirectory(input, output)
  }
}
