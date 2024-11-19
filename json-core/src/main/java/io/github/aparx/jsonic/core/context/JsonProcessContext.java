package io.github.aparx.jsonic.core.context;

import org.checkerframework.checker.index.qual.NonNegative;

/**
 * @author aparx (Vinzent Z.)
 * @version 2024-11-18 23:42
 * @since 1.0
 */
public interface JsonProcessContext {

  void newline();

  void append(char ch);

  @NonNegative
  int getLineIndex();

  @NonNegative
  int getPositionInLine();

}
