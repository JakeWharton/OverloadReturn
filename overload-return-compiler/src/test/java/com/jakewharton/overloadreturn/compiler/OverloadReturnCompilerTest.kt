package com.jakewharton.overloadreturn.compiler

import com.google.common.truth.Truth.assertThat
import com.google.testing.compile.JavaFileObjects
import org.junit.Ignore
import org.junit.Test

class OverloadReturnCompilerTest {
  private val compiler = OverloadReturnCompiler()

  @Test fun instanceMethodToVoid() {
    val input = JavaFileObjects.forSourceString("com.example.Test", """
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

    val inputBytes = compile(input).single().toByteArray()
    val outputBytes = compiler.parse(inputBytes).toBytes()
    val output = outputBytes.toBytecodeString()

    assertThat(output).isEqualTo("""
      // class version 52.0 (52)
      // access flags 0x31
      public final class com/example/Test {

        // compiled from: Test.java

        // access flags 0x1
        public <init>()V
         L0
          LINENUMBER 5 L0
          ALOAD 0
          INVOKESPECIAL java/lang/Object.<init> ()V
          RETURN
          MAXSTACK = 1
          MAXLOCALS = 1

        // access flags 0x1
        public method()I
         L0
          LINENUMBER 8 L0
          ICONST_0
          IRETURN
          MAXSTACK = 1
          MAXLOCALS = 1

        // access flags 0x1
        public expected()V
         L0
          LINENUMBER 12 L0
          ALOAD 0
          INVOKEVIRTUAL com/example/Test.method ()I
          POP
         L1
          LINENUMBER 13 L1
          RETURN
          MAXSTACK = 1
          MAXLOCALS = 1

        // access flags 0x1041
        public synthetic bridge method()V
          ALOAD 0
          INVOKEVIRTUAL com/example/Test.method ()I
          POP
          RETURN
          MAXSTACK = 1
          MAXLOCALS = 1
      }
      """.trimIndent())
  }

  @Test fun staticMethodToVoid() {
    val input = JavaFileObjects.forSourceString("com.example.Test", """
      package com.example;

      import com.jakewharton.overloadreturn.OverloadReturn;

      public final class Test {
        @OverloadReturn(void.class)
        public static int method() {
          return 0;
        }

        public static void expected() {
          method();
        }
      }
    """.trimIndent())

    val inputBytes = compile(input).single().toByteArray()
    val outputBytes = compiler.parse(inputBytes).toBytes()
    val output = outputBytes.toBytecodeString()

    assertThat(output).isEqualTo("""
      // class version 52.0 (52)
      // access flags 0x31
      public final class com/example/Test {

        // compiled from: Test.java

        // access flags 0x1
        public <init>()V
         L0
          LINENUMBER 5 L0
          ALOAD 0
          INVOKESPECIAL java/lang/Object.<init> ()V
          RETURN
          MAXSTACK = 1
          MAXLOCALS = 1

        // access flags 0x9
        public static method()I
         L0
          LINENUMBER 8 L0
          ICONST_0
          IRETURN
          MAXSTACK = 1
          MAXLOCALS = 0

        // access flags 0x9
        public static expected()V
         L0
          LINENUMBER 12 L0
          INVOKESTATIC com/example/Test.method ()I
          POP
         L1
          LINENUMBER 13 L1
          RETURN
          MAXSTACK = 1
          MAXLOCALS = 0

        // access flags 0x1049
        public static synthetic bridge method()V
          INVOKESTATIC com/example/Test.method ()I
          POP
          RETURN
          MAXSTACK = 1
          MAXLOCALS = 0
      }
      """.trimIndent())
  }

  @Test fun instanceMethodCovariant() {
    val input = JavaFileObjects.forSourceString("com.example.Test", """
      package com.example;

      import com.jakewharton.overloadreturn.OverloadReturn;

      public final class Test {
        @OverloadReturn(CharSequence.class)
        public String method() {
          return "";
        }

        public CharSequence expected() {
          return method();
        }
      }
    """.trimIndent())

    val inputBytes = compile(input).single().toByteArray()
    val outputBytes = compiler.parse(inputBytes).toBytes()
    val output = outputBytes.toBytecodeString()

    assertThat(output).isEqualTo("""
      // class version 52.0 (52)
      // access flags 0x31
      public final class com/example/Test {

        // compiled from: Test.java

        // access flags 0x1
        public <init>()V
         L0
          LINENUMBER 5 L0
          ALOAD 0
          INVOKESPECIAL java/lang/Object.<init> ()V
          RETURN
          MAXSTACK = 1
          MAXLOCALS = 1

        // access flags 0x1
        public method()Ljava/lang/String;
         L0
          LINENUMBER 8 L0
          LDC ""
          ARETURN
          MAXSTACK = 1
          MAXLOCALS = 1

        // access flags 0x1
        public expected()Ljava/lang/CharSequence;
         L0
          LINENUMBER 12 L0
          ALOAD 0
          INVOKEVIRTUAL com/example/Test.method ()Ljava/lang/String;
          ARETURN
          MAXSTACK = 1
          MAXLOCALS = 1

        // access flags 0x1041
        public synthetic bridge method()Ljava/lang/CharSequence;
          ALOAD 0
          INVOKEVIRTUAL com/example/Test.method ()Ljava/lang/String;
          ARETURN
          MAXSTACK = 1
          MAXLOCALS = 1
      }
      """.trimIndent())
  }

  @Test fun staticMethodCovariant() {
    val input = JavaFileObjects.forSourceString("com.example.Test", """
      package com.example;

      import com.jakewharton.overloadreturn.OverloadReturn;

      public final class Test {
        @OverloadReturn(CharSequence.class)
        public static String method() {
          return "";
        }

        public static CharSequence expected() {
          return method();
        }
      }
    """.trimIndent())

    val inputBytes = compile(input).single().toByteArray()
    val outputBytes = compiler.parse(inputBytes).toBytes()
    val output = outputBytes.toBytecodeString()

    assertThat(output).isEqualTo("""
      // class version 52.0 (52)
      // access flags 0x31
      public final class com/example/Test {

        // compiled from: Test.java

        // access flags 0x1
        public <init>()V
         L0
          LINENUMBER 5 L0
          ALOAD 0
          INVOKESPECIAL java/lang/Object.<init> ()V
          RETURN
          MAXSTACK = 1
          MAXLOCALS = 1

        // access flags 0x9
        public static method()Ljava/lang/String;
         L0
          LINENUMBER 8 L0
          LDC ""
          ARETURN
          MAXSTACK = 1
          MAXLOCALS = 0

        // access flags 0x9
        public static expected()Ljava/lang/CharSequence;
         L0
          LINENUMBER 12 L0
          INVOKESTATIC com/example/Test.method ()Ljava/lang/String;
          ARETURN
          MAXSTACK = 1
          MAXLOCALS = 0

        // access flags 0x1049
        public static synthetic bridge method()Ljava/lang/CharSequence;
          INVOKESTATIC com/example/Test.method ()Ljava/lang/String;
          ARETURN
          MAXSTACK = 1
          MAXLOCALS = 0
      }
      """.trimIndent())
  }

  @Test fun argumentTypes() {
    val input = JavaFileObjects.forSourceString("com.example.Test", """
      package com.example;

      import com.jakewharton.overloadreturn.OverloadReturn;

      public final class Test {
        @OverloadReturn(CharSequence.class)
        public String method(boolean a, byte b, char c, double d, float f, int i, long l, short s, Object o) {
          return "hey";
        }

        public CharSequence expected(boolean a, byte b, char c, double d, float f, int i, long l, short s, Object o) {
          return method(a, b, c, d, f, i, l, s, o);
        }
      }
    """.trimIndent())

    val inputBytes = compile(input).single().toByteArray()
    val outputBytes = compiler.parse(inputBytes).toBytes()
    val output = outputBytes.toBytecodeString()

    assertThat(output).isEqualTo("""
      // class version 52.0 (52)
      // access flags 0x31
      public final class com/example/Test {

        // compiled from: Test.java

        // access flags 0x1
        public <init>()V
         L0
          LINENUMBER 5 L0
          ALOAD 0
          INVOKESPECIAL java/lang/Object.<init> ()V
          RETURN
          MAXSTACK = 1
          MAXLOCALS = 1

        // access flags 0x1
        public method(ZBCDFIJSLjava/lang/Object;)Ljava/lang/String;
         L0
          LINENUMBER 8 L0
          LDC "hey"
          ARETURN
          MAXSTACK = 1
          MAXLOCALS = 12

        // access flags 0x1
        public expected(ZBCDFIJSLjava/lang/Object;)Ljava/lang/CharSequence;
         L0
          LINENUMBER 12 L0
          ALOAD 0
          ILOAD 1
          ILOAD 2
          ILOAD 3
          DLOAD 4
          FLOAD 6
          ILOAD 7
          LLOAD 8
          ILOAD 10
          ALOAD 11
          INVOKEVIRTUAL com/example/Test.method (ZBCDFIJSLjava/lang/Object;)Ljava/lang/String;
          ARETURN
          MAXSTACK = 12
          MAXLOCALS = 12

        // access flags 0x1041
        public synthetic bridge method(ZBCDFIJSLjava/lang/Object;)Ljava/lang/CharSequence;
          ALOAD 0
          ILOAD 1
          ILOAD 2
          ILOAD 3
          DLOAD 4
          FLOAD 6
          ILOAD 7
          LLOAD 8
          ILOAD 10
          ALOAD 11
          INVOKEVIRTUAL com/example/Test.method (ZBCDFIJSLjava/lang/Object;)Ljava/lang/String;
          ARETURN
          MAXSTACK = 12
          MAXLOCALS = 12
      }
      """.trimIndent())
  }

  @Ignore("Wrong signature and declaration")
  @Test fun genericMethod() {
    val input = JavaFileObjects.forSourceString("com.example.Test", """
      package com.example;

      import com.jakewharton.overloadreturn.OverloadReturn;
      import java.util.Collection;
      import java.util.List;

      public final class Test {
        @OverloadReturn(Collection.class)
        public List<String> method() {
          return null;
        }

        public Collection<String> expected() {
          return method();
        }
      }
    """.trimIndent())

    val inputBytes = compile(input).single().toByteArray()
    val outputBytes = compiler.parse(inputBytes).toBytes()
    val output = outputBytes.toBytecodeString()

    assertThat(output).isEqualTo("""
      // class version 52.0 (52)
      // access flags 0x31
      public final class com/example/Test {

        // compiled from: Test.java

        // access flags 0x1
        public <init>()V
         L0
          LINENUMBER 7 L0
          ALOAD 0
          INVOKESPECIAL java/lang/Object.<init> ()V
          RETURN
          MAXSTACK = 1
          MAXLOCALS = 1

        // access flags 0x1
        // signature ()Ljava/util/List<Ljava/lang/String;>;
        // declaration: java.util.List<java.lang.String> method()
        public method()Ljava/util/List;
         L0
          LINENUMBER 10 L0
          ACONST_NULL
          ARETURN
          MAXSTACK = 1
          MAXLOCALS = 1

        // access flags 0x1
        // signature ()Ljava/util/Collection<Ljava/lang/String;>;
        // declaration: java.util.Collection<java.lang.String> expected()
        public expected()Ljava/util/Collection;
         L0
          LINENUMBER 14 L0
          ALOAD 0
          INVOKEVIRTUAL com/example/Test.method ()Ljava/util/List;
          ARETURN
          MAXSTACK = 1
          MAXLOCALS = 1

        // access flags 0x1001
        // signature ()Ljava/util/List<Ljava/lang/String;>;
        // declaration: java.util.List<java.lang.String> method()
        public synthetic method()Ljava/util/Collection;
          ALOAD 0
          INVOKEVIRTUAL com/example/Test.method ()Ljava/util/List;
          ARETURN
          MAXSTACK = 1
          MAXLOCALS = 1
      }
      """.trimIndent())
  }
}
