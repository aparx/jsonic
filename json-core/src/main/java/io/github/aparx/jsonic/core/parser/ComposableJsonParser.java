package io.github.aparx.jsonic.core.parser;

import io.github.aparx.jsonic.core.parser.source.JsonCharSourceTraverser;
import io.github.aparx.jsonic.core.parser.syntax.JsonSyntaxReader;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.common.value.qual.IntRange;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * @author aparx (Vinzent Z.)
 * @version 2024-11-15 16:09
 * @since 1.0
 */
@DefaultQualifier(NonNull.class)
public interface ComposableJsonParser<T> extends JsonParser<T> {

  /**
   * Returns true if this parser can <i>potentially</i> be used to parse {@code currentChar} and
   * following characters with a higher likelihood of it succeeding, compared to simply parsing
   * right away.
   * <p>This function basically identifies this parser for two consecutive characters and can be
   * used to determine which parser to use in a composed set of parsers.
   * <p>This function is idempotent and pure.
   *
   * @param currentChar the current character, for example from a traverser
   * @param nextChar    the potential next character in the source, or {@code -1} if not available
   * @return true if this parser might have a higher chance of succeeding based on given inputs
   */
  boolean couldParse(char currentChar, @IntRange(from = -1, to = Character.MAX_VALUE) int nextChar);

  /**
   * Returns a new parser that automatically determines which one of given parsers to use, using
   * the {@code couldParse} method with the current and peeked character of the input traverser.
   * <p>If two parsers have an equal {@code couldParse} implementation or both would return true
   * for the same inputs, the first in order is used. Thus, it is very important to ensure that
   * the passed parsers differ in their identification process.
   *
   * @param parsers the parsers to be composed into one
   * @return parser composing all given parsers into one, determining which one to use by using
   * the {@link #couldParse(char, int)} method.
   * @see #couldParse(char, int)
   * @see #compose(Function)
   */
  static JsonParser<?> compose(ComposableJsonParser<?>... parsers) {
    return ComposableJsonParser.compose((__) -> List.of(parsers));
  }

  /**
   * Returns a new parser that automatically determines which one of given parsers to use, using
   * the {@code couldParse} method with the current and peeked character of the input traverser.
   * <p>If two parsers have an equal {@code couldParse} implementation or both would return true
   * for the same inputs, the first in order is used. Thus, it is very important to ensure that
   * the passed parsers differ in their identification process.
   *
   * @param factory function that returns the array of parsers to compose, accepting an argument
   *                representing the returned composed parser (allows self-references and
   *                recursion).
   * @return parser composing all given parsers into one, determining which one to use by using
   * the {@link #couldParse(char, int)} method.
   * @see #couldParse(char, int)
   * @see #compose(ComposableJsonParser[])
   */
  static JsonParser<?> compose(Function<JsonParser<?>, Collection<ComposableJsonParser<?>>> factory) {
    return new JsonParser<>() {

      private final Collection<ComposableJsonParser<?>> parsers = factory.apply(this);

      @Override
      public @Nullable Object parse(JsonCharSourceTraverser traverser, JsonSyntaxReader reader) {
        return (parsers.stream()
            .filter((x) -> x.couldParse(traverser.current(), traverser.peek()))
            .findFirst()
            .orElseThrow(() -> {
              String message = String.format("Unexpected token: %s", traverser.current());
              return reader.errorFactory().create(reader, traverser, message);
            })).parse(traverser, reader);
      }
    };
  }
}
