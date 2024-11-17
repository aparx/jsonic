package io.github.aparx.jsonic.core;

/**
 * @author aparx (Vinzent Z.)
 * @version 2024-11-15 14:16
 * @since 1.0
 */
public enum JsonSymbol {

  DOUBLE_QUOTE('"'),
  CURLY_OPEN('{'),
  CURLY_CLOSE('}'),
  SQUARE_OPEN('['),
  SQUARE_CLOSE(']'),
  COMMA(','),
  COLON(':');

  private final char literal;

  JsonSymbol(char literal) {
    this.literal = literal;
  }

  public char literal() {
    return literal;
  }

  public boolean matches(int character) {
    return this.literal == character;
  }

}
