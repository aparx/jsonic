package io.github.aparx.jsonic.core.parser;

import io.github.aparx.jsonic.core.parser.error.JsonParseError;
import io.github.aparx.jsonic.core.parser.source.JsonCharSourceTraverser;
import io.github.aparx.jsonic.core.parser.source.JsonCharSourceTraversers;
import io.github.aparx.jsonic.core.parser.syntax.DefaultJsonSyntaxReader;
import io.github.aparx.jsonic.core.parser.syntax.JsonSyntaxReader;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.com.google.common.base.Preconditions;
import org.checkerframework.com.google.errorprone.annotations.CheckReturnValue;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.NoSuchElementException;

/**
 * @author aparx (Vinzent Z.)
 * @version 2024-11-15 14:21
 * @since 1.0
 */
@FunctionalInterface
@DefaultQualifier(NonNull.class)
public interface JsonParser<T> {

  /**
   * Reads and parses {@code traverser} into an object {@code T}, or throws an error when the
   * traverser could not be parsed as intended.
   * <p>This parser begins with the current character and ends with the last character valid for
   * this parser, commonly being a JsonSymbol determining the exit of this parser.
   * <p>Example: Assuming we allow to parse a JSON string, following this syntax:
   * {@code Pattern.compile("\"[a-z]*\"")} the following code results in following:
   * <pre><code>
   *   var traverser = JsonCharSourceTraverserFactory.read("\"hello\"");
   *   parse(traverser, syntaxReader);    ::= "hello"
   *   ctx.current(); ::= '"'
   *
   *   var traverser = JsonCharSourceTraverserFactory.read("\"hellO\"");
   *   parse(traverser, syntaxReader);    ::= should throw error
   *   traverser.current(); ::= 'l'
   * </code></pre>
   *
   * @param traverser    the traverser, supplying the data necessary to parse
   * @param syntaxReader the syntax reader used to read and validate specific parts of the traverser
   * @return the parsed object, {@code nullable}
   * @throws JsonParseError         if {@code traverser} could not be parsed as intended
   * @throws NoSuchElementException if no characters were left in {@code traverser}
   */
  @CheckReturnValue
  @Nullable T parse(JsonCharSourceTraverser traverser, JsonSyntaxReader syntaxReader);

  static <@Nullable T> @Nullable T parse(JsonParser<T> parser, JsonCharSourceTraverser traverser) {
    Preconditions.checkState(traverser.hasRead(), "Traverser has not been read");
    return parser.parse(traverser, DefaultJsonSyntaxReader.DEFAULT);
  }

  static <@Nullable T> @Nullable T parse(JsonParser<T> parser, JsonCharSourceTraverser traverser,
                                         JsonSyntaxReader syntaxReader) {
    Preconditions.checkState(traverser.hasRead(), "Traverser has not been read");
    return parser.parse(traverser, syntaxReader);
  }

  static <@Nullable T> @Nullable T parse(JsonParser<T> parser, CharSequence sequence) {
    return parse(parser, JsonCharSourceTraversers.read(sequence));
  }

  static <@Nullable T> @Nullable T parse(
      JsonParser<T> parser, CharSequence sequence, JsonSyntaxReader syntaxReader) {
    return parse(parser, JsonCharSourceTraversers.read(sequence), syntaxReader);
  }

  static <@Nullable T> @Nullable T parse(JsonParser<T> parser, InputStream inputStream) {
    return parse(parser, JsonCharSourceTraversers.read(inputStream));
  }

  static <@Nullable T> @Nullable T parse(
      JsonParser<T> parser, InputStream inputStream, JsonSyntaxReader syntaxReader) {
    return parse(parser, JsonCharSourceTraversers.read(inputStream), syntaxReader);
  }

  static <@Nullable T> @Nullable T parse(
      JsonParser<T> parser, File file
  ) throws FileNotFoundException {
    return parse(parser, JsonCharSourceTraversers.read(file));
  }

  static <@Nullable T> @Nullable T parse(
      JsonParser<T> parser, File file, JsonSyntaxReader syntaxReader
  ) throws FileNotFoundException {
    return parse(parser, JsonCharSourceTraversers.read(file), syntaxReader);
  }

}
