package io.github.aparx.jsonic.core.parser.context;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.com.google.common.base.Preconditions;
import org.checkerframework.com.google.errorprone.annotations.CheckReturnValue;
import org.checkerframework.common.value.qual.IntRange;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.io.*;
import java.util.NoSuchElementException;

/**
 * @author aparx (Vinzent Z.)
 * @version 2024-11-15 14:37
 * @since 1.0
 */
@DefaultQualifier(NonNull.class)
public final class JsonCharSourceFactory {

  private JsonCharSourceFactory() {
    throw new AssertionError();
  }

  @CheckReturnValue
  public static JsonCharSource from(CharSequence sequence, int offset, int length) {
    Preconditions.checkArgument(!sequence.isEmpty(), "Sequence must not be empty");
    Preconditions.checkPositionIndex(length, sequence.length());
    Preconditions.checkElementIndex(offset, length);
    return new SequenceSource(sequence, offset, length);
  }

  @CheckReturnValue
  public static JsonCharSource from(CharSequence sequence, int offset) {
    Preconditions.checkArgument(!sequence.isEmpty(), "Sequence must not be empty");
    Preconditions.checkElementIndex(offset, sequence.length());
    return new SequenceSource(sequence, offset, sequence.length());
  }

  @CheckReturnValue
  public static JsonCharSource from(CharSequence sequence) {
    Preconditions.checkArgument(!sequence.isEmpty(), "Sequence must not be empty");
    return new SequenceSource(sequence, 0, sequence.length());
  }

  @CheckReturnValue
  public static JsonCharSource from(InputStream inputStream) {
    return new InputStreamSource(inputStream);
  }

  @CheckReturnValue
  public static JsonCharSource from(File file) throws FileNotFoundException {
    return new InputStreamSource(new FileInputStream(file));
  }

  private static final class SequenceSource implements JsonCharSource {

    private final CharSequence sequence;
    private final int length;
    private int position;

    public SequenceSource(CharSequence sequence, int offset, int length) {
      Preconditions.checkNotNull(sequence, "Sequence must not be null");
      this.sequence = sequence;
      this.position = offset - 1;
      this.length = length;
    }

    @Override
    public char next() {
      if (!this.hasNext())
        throw new NoSuchElementException("Source is exhausted");
      return this.sequence.charAt(++this.position);
    }

    @Override
    public boolean hasNext() {
      return this.position < this.length - 1;
    }
  }

  // TODO: test
  private static final class InputStreamSource implements JsonCharSource {

    private final InputStream stream;

    @IntRange(from = -1, to = Character.MAX_VALUE)
    private int peekedChar = -1;
    private boolean peekDirty = true;

    public InputStreamSource(InputStream stream) {
      Preconditions.checkNotNull(stream, "Stream must not be null");
      this.stream = stream;
    }

    @Override
    public char next() {
      try {
        int peek = this.peek();
        this.peekDirty = true;
        if (peek >= 0) return (char) peek;
        throw new NoSuchElementException("Source is exhausted");
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    @Override
    public boolean hasNext() {
      try {
        return this.peek() >= 0;
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    private int peek() throws IOException {
      if (!this.peekDirty)
        return this.peekedChar;
      this.peekedChar = stream.read();
      this.peekDirty = false;
      return this.peekedChar;
    }

  }

}
