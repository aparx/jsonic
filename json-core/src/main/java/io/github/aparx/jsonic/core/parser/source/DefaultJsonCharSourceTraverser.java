package io.github.aparx.jsonic.core.parser.source;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import io.github.aparx.jsonic.core.context.JsonProcessContext;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.com.google.common.base.Preconditions;
import org.checkerframework.common.value.qual.IntRange;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.checkerframework.org.apache.commons.lang3.SystemUtils;

import java.util.NoSuchElementException;

/**
 * @author aparx (Vinzent Z.)
 * @version 2024-11-18 23:43
 * @since 1.0
 */
@DefaultQualifier(NonNull.class)
public final class DefaultJsonCharSourceTraverser implements JsonCharSourceTraverser {

  private final JsonCharSource source;
  private final JsonProcessContext context;

  @IntRange(from = NULL_CHARACTER, to = Character.MAX_VALUE)
  private int peekedChar = NULL_CHARACTER;

  @IntRange(from = NULL_CHARACTER, to = Character.MAX_VALUE)
  private int currentChar = NULL_CHARACTER;

  private boolean hasPeeked;

  public DefaultJsonCharSourceTraverser(JsonCharSource source, JsonProcessContext context) {
    Preconditions.checkNotNull(source, "Source must not be null");
    Preconditions.checkNotNull(context, "Context must not be null");
    this.source = source;
    this.context = context;
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
    if (peekedChar == NULL_CHARACTER)
      // This block should never be reached, and if: the underlying source is wrong
      throw new NoSuchElementException("Source is exhausted");
    int lastChar = this.currentChar;
    this.currentChar = peekedChar;
    this.hasPeeked = false;
    this.context.append((char) this.currentChar);
    if (this.currentChar == '\n' && (!SystemUtils.IS_OS_LINUX || lastChar == '\r'))
      this.context.newline();
    return (char) this.currentChar;
  }

  @Override
  public boolean hasRead() {
    return this.currentChar != NULL_CHARACTER;
  }

  @CanIgnoreReturnValue
  @IntRange(from = NULL_CHARACTER, to = Character.MAX_VALUE)
  public int peek() {
    if (hasPeeked) return this.peekedChar;
    if (!this.source.hasNext())
      return NULL_CHARACTER;
    this.hasPeeked = true;
    return (this.peekedChar = this.source.next());
  }

  public char current() {
    if (this.currentChar == NULL_CHARACTER)
      throw new NoSuchElementException("Context has not been read yet");
    return (char) this.currentChar;
  }

  @Override
  public JsonProcessContext context() {
    return this.context;
  }
}
