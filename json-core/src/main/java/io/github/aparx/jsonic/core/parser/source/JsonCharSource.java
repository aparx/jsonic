package io.github.aparx.jsonic.core.parser.source;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Iterator-like interface, providing a function to iterate to the next primitive character in
 * a source of characters. This interface acts as a substitute for the {@code Iterator} interface,
 * but uses an alike protocol.
 *
 * @author aparx (Vinzent Z.)
 * @version 2024-11-15 14:26
 * @see Iterator
 * @see JsonCharSourceFactory
 * @since 1.0
 */
@DefaultQualifier(NonNull.class)
public interface JsonCharSource {


  /**
   * Returns true, if there is at least one more character left in the source.
   *
   * @return true if there is more than zero characters left to be iterated upon
   * @see #next()
   */
  boolean hasNext();

  /**
   * Skips to the next character in the source and returns it.
   *
   * @return the next character in the source
   * @throws NoSuchElementException if the source has no more characters
   * @see #hasNext()
   */
  char next();

}
