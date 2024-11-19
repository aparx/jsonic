package io.github.aparx.jsonic.core.context;

import org.checkerframework.checker.index.qual.Positive;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.com.google.common.base.Preconditions;

/**
 * A circular history buffer implementation of DefaultJsonProcessContext.
 * <p>This history implementation is constant-time on insertions and linear on read, thus making
 * writing faster than reading. This is very helpful, since the history is usually only read when
 * an error occurs, and is thus rarer than insertion, which happens very frequently (especially
 * with large datasets).
 *
 * @author aparx (Vinzent Z.)
 * @version 2024-11-19 00:32
 * @since 1.0
 */
public class JsonProcessHistoryContext extends JsonProcessSimpleContext {

  public static final int DEFAULT_BUFFER_SIZE = 32;

  @Positive
  private final int bufferLength;
  private final char[] buffer;

  private int head;
  private int size;

  private @Nullable String cached;

  public JsonProcessHistoryContext() {
    this(DEFAULT_BUFFER_SIZE);
  }

  public JsonProcessHistoryContext(int bufferLength) {
    Preconditions.checkArgument(bufferLength >= 1, "Buffer length must be greater than zero");
    this.bufferLength = bufferLength;
    this.buffer = new char[bufferLength];
  }

  /**
   * {@inheritDoc}
   * <p>The underlying buffer head and size is automatically reset.
   */
  @Override
  public void newline() {
    super.newline();
    this.head = 0;
    this.size = 0;
    this.cached = null;
  }

  /**
   * {@inheritDoc}
   * <p>Given character is added to the buffer at the current insertion position. If the buffer
   * is full, the oldest character is automatically overwritten to make room for the new one,
   * ensuring a fixed-size history.
   *
   * @param ch {@inheritDoc}
   * @implNote The time-complexity and space-complexity are {@code O(1)}, making "appending" faster
   * than simply reading the history.
   */
  @Override
  public void append(char ch) {
    super.append(ch);
    this.buffer[this.head] = ch;
    this.head = (1 + head) % bufferLength;
    if (this.size < bufferLength) ++this.size;
    this.cached = null;
  }

  /**
   * Returns the complete valid history of this history buffer, depending on the head and size.
   * <p>The maximum total length of the returning string is equal to this buffer's size.
   *
   * @return the complete history represented as a string
   * @implNote The time- and space-complexity are {@code O(n)} at worst case,
   * where {@code n} is the size (not capacity) of the history. This makes reading <i>slower</i>
   * than writing. The output is cached and the cache invalidated once the buffer is changed.
   * @see #getHistory(int)
   */
  public String getHistory() {
    if (this.cached != null)
      return this.cached;
    // Ensure a fast alternative for the rare case the internal buffer equals the output
    if (this.size == this.bufferLength - 1 && this.head == 0)
      return new String(this.buffer);
    // Accumulate the new string, representing the history, based on the "circular" buffer
    char[] array = new char[this.size];
    for (int i = 0; i < this.size; ++i)
      array[i] = buffer[(head - size + i + bufferLength) % bufferLength];
    this.cached = new String(array);
    return this.cached;
  }

  /**
   * Returns the valid history of this buffer and limits the length to given max length.
   * <p>If the complete history's string representation is longer than {@code maxLength}, the
   * complete representation is offset by the difference of those values.
   *
   * @param maxLength the maximum length the returning history should have.
   * @return the last characters in this history, with a max length of {@code maxLength}
   * @implSpec This implementation utilizes this {@link #getHistory()}, thus has the same side
   * effects (& more) and the same time- and space-complexity.
   * @see #getHistory()
   */
  public String getHistory(int maxLength) {
    Preconditions.checkArgument(maxLength >= 0, "Max length must be positive");
    Preconditions.checkArgument(maxLength <= this.bufferLength, "Max length > Buffer length");
    String completeHistory = this.getHistory();
    int completeLength = completeHistory.length();
    if (completeLength > maxLength)
      return completeHistory.substring(completeLength - maxLength);
    return completeHistory;
  }

  public int size() {
    return size;
  }

  @Positive
  public int getBufferLength() {
    return bufferLength;
  }
}
