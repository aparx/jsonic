package io.github.aparx.jsonic.core.parser;

import io.github.aparx.jsonic.core.parser.context.JsonParseContext;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.com.google.errorprone.annotations.CheckReturnValue;
import org.checkerframework.framework.qual.DefaultQualifier;

/**
 * @author aparx (Vinzent Z.)
 * @version 2024-11-15 14:21
 * @since 1.0
 */
@FunctionalInterface
@DefaultQualifier(NonNull.class)
public interface JsonParser<T> {

  /**
   * Reads and parses {@code context} into an object {@code T}, or throws an error when the context
   * could not be parsed as intended.
   * <p>This parser begins with the current character and ends with the last character valid for
   * this parser, commonly being a JsonSymbol determining the exit of this parser.
   * <p>Example: Assuming we allow to parse a JSON string, following this syntax:
   * {@code Pattern.compile("\"[a-z]*\"")} the following code results in following:
   * <pre><code>
   *   var ctx = JsonParseContextFactory.read("\"hello\"");
   *   parse(ctx);    ::= "hello"
   *   ctx.current(); ::= '"'
   *
   *   var ctx = JsonParseContextFactory.read("\"hellO\"");
   *   parse(ctx);    ::= should throw error
   *   ctx.current(); ::= 'l'
   * </code></pre>
   *
   * @param context the context, supplying the data necessary to parse
   * @return the parsed object, {@code nullable}
   */
  @CheckReturnValue
  @Nullable T parse(JsonParseContext context);

}
