package com.jakewharton.overloadreturn.gradle

import com.google.common.truth.Truth.assertThat
import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters
import java.io.File
import java.util.Properties

@RunWith(Parameterized::class)
class FixturesTest(val fixtureRoot: File, val name: String) {
  @Test fun run() {
    val androidHome = androidHome()
    File(fixtureRoot, "local.properties").writeText("sdk.dir=$androidHome\n")

    val runner = GradleRunner.create()
        .withProjectDir(fixtureRoot)
        .withPluginClasspath()
        .withArguments("clean", "assemble", "--stacktrace")

    val expectedFailure = File(fixtureRoot, "failure.txt")
    if (expectedFailure.exists()) {
      val result = runner.buildAndFail()
      println(result.output)
      for (chunk in expectedFailure.readText().split("\n\n")) {
        assertThat(result.output).contains(chunk)
      }
    } else {
      val result = runner.build()
      println(result.output)
      assertThat(result.output).contains("BUILD SUCCESSFUL")
    }
  }

  companion object {
    @Suppress("unused") // Used by Parameterized JUnit runner reflectively.
    @Parameters(name = "{1}")
    @JvmStatic
    fun parameters() = File("src/test/fixtures").listFiles()
        .filter { it.isDirectory }
        .map { arrayOf(it, it.name) }
  }
}

internal fun androidHome(): String {
  val env = System.getenv("ANDROID_HOME")
  if (env != null) {
    return env
  }
  val localProp = File(File(System.getProperty("user.dir")).parentFile, "local.properties")
  if (localProp.exists()) {
    val prop = Properties()
    localProp.inputStream().use {
      prop.load(it)
    }
    val sdkHome = prop.getProperty("sdk.dir")
    if (sdkHome != null) {
      return sdkHome
    }
  }
  throw IllegalStateException(
      "Missing 'ANDROID_HOME' environment variable or local.properties with 'sdk.dir'")
}
