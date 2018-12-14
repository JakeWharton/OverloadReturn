package com.example;

import com.jakewharton.overloadreturn.OverloadReturn;

public final class Test {
  @OverloadReturn(CharSequence.class)
  public String greeting() {
    return "hello";
  }
}
