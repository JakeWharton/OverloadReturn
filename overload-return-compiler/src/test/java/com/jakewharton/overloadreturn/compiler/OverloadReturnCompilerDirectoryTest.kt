package com.jakewharton.overloadreturn.compiler

import com.google.common.jimfs.Configuration
import com.google.common.jimfs.Jimfs
import com.google.common.truth.Truth.assertThat
import com.google.testing.compile.JavaFileObjects
import org.junit.Test
import javax.tools.JavaFileObject

class OverloadReturnCompilerDirectoryTest {
  private val compiler = OverloadReturnCompiler()
  private val fs = Jimfs.newFileSystem(Configuration.unix())
  private val input = fs.rootDirectories.first().resolve("in").also { it.createDirectories() }
  private val output = fs.rootDirectories.first().resolve("out").also { it.createDirectories() }

  @Test fun nonClassFilesAreCopied() {
    input.resolve("test.txt").writeText("hey")
    compiler.processDirectory(input, output)
    assertThat(output.resolve("test.txt").readText()).isEqualTo("hey")
  }

  @Test fun classFileProcessed() {
    val inputJava = JavaFileObjects.forSourceString("com.example.Test", """
      package com.example;

      import com.jakewharton.overloadreturn.OverloadReturn;

      public final class Test {
        @OverloadReturn(void.class)
        public int method() {
          return 0;
        }

        public void expected() {
          method();
        }
      }
    """.trimIndent())
    compileInputs(inputJava)

    compiler.processDirectory(input, output)

    val outputClass = output.resolve("com/example/Test.class")
    assertThat(outputClass.exists()).isTrue()
  }

  private fun compileInputs(vararg files: JavaFileObject) {
    compile(*files).forEach { byteCode ->
      val target = input.resolve(byteCode.name.removePrefix("/CLASS_OUTPUT/"))
      target.parent.createDirectories()
      target.writeBytes(byteCode.toByteArray())
    }
  }
}
