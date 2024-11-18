package io.github.aparx.jsonic.core.parser.context;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import io.github.aparx.jsonic.core.JsonSymbol;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.com.google.common.base.Preconditions;
import org.checkerframework.common.value.qual.IntRange;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.checkerframework.org.apache.commons.lang3.SystemUtils;

import java.util.NoSuchElementException;

/**
 * @author aparx (Vinzent Z.)
 * @version 2024-11-15 14:21
 * @see JsonParseContextFactory
 * @since 1.0
 */
@DefaultQualifier(NonNull.class)
public class JsonParseContext implements JsonCharSource {

  /** Integer representing an illegal character (representing a void, thus "null", character) */
  private static final int NULL_CHARACTER = -1;

  private final JsonCharSource source;
  private final JsonSyntaxReader syntaxReader;

  @IntRange(from = JsonParseContext.NULL_CHARACTER, to = Character.MAX_VALUE)
  private int peekedChar = JsonParseContext.NULL_CHARACTER;

  @IntRange(from = JsonParseContext.NULL_CHARACTER, to = Character.MAX_VALUE)
  private int currentChar = JsonParseContext.NULL_CHARACTER;

  private boolean hasPeeked;

  private int charPosInLine, lineIndex;

  protected JsonParseContext(JsonCharSource source, JsonSyntaxReader syntaxReader) {
    Preconditions.checkNotNull(source, "Source must not be null");
    Preconditions.checkNotNull(syntaxReader, "Syntax reader must not be null");
    this.source = source;
    this.syntaxReader = syntaxReader;
  }

  @Override
  public boolean hasNext() {
    return this.hasPeeked || this.source.hasNext();
  }

  @Override
  public char next() {
    // In order to check if we have a new line, we need to ensure
    // that we have a current and next value (to check against "\n" or "\r\n").
    int peekedChar = this.peek();
    if (peekedChar == JsonParseContext.NULL_CHARACTER)
      // This block should never be reached, and if: the underlying source is wrong
      throw new NoSuchElementException("Source is exhausted");
    int lastChar = this.currentChar;
    this.currentChar = peekedChar;
    this.hasPeeked = false;
    ++this.charPosInLine;
    if (this.currentChar == '\n' && (!SystemUtils.IS_OS_LINUX || lastChar == '\r')) {
      ++this.lineIndex; // We have begun a new line, mark it in the counter
      this.charPosInLine = 0;
    }
    return (char) this.currentChar;
  }

  @CanIgnoreReturnValue
  @IntRange(from = JsonParseContext.NULL_CHARACTER, to = Character.MAX_VALUE)
  public int peek() {
    if (hasPeeked) return this.peekedChar;
    if (!this.source.hasNext())
      return JsonParseContext.NULL_CHARACTER;
    this.hasPeeked = true;
    return (this.peekedChar = this.source.next());
  }

  public char current() {
    if (this.currentChar == JsonParseContext.NULL_CHARACTER)
      throw new NoSuchElementException("Context has not been read yet");
    return (char) this.currentChar;
  }

  /** @deprecated Use {@link JsonSymbol#matches(int)} instead */
  @Deprecated
  public boolean isSymbol(JsonSymbol symbol) {
    return symbol.matches(this.current());
  }

  public int charPosInLine() {
    return charPosInLine;
  }

  public int lineIndex() {
    return lineIndex;
  }

  public JsonCharSource source() {
    return source;
  }

  public JsonSyntaxReader syntaxReader() {
    return syntaxReader;
  }
}
