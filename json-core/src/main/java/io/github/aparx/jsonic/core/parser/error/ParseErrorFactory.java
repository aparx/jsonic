package io.github.aparx.jsonic.core.parser.error;

import io.github.aparx.jsonic.core.error.JsonicErrorMessageFactory;
import io.github.aparx.jsonic.core.parser.source.JsonCharSourceTraverser;
import io.github.aparx.jsonic.core.parser.syntax.JsonSyntaxReader;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

/**
 * @author aparx (Vinzent Z.)
 * @version 2024-11-15 15:30
 * @since 1.0
 */
@FunctionalInterface
@DefaultQualifier(NonNull.class)
public interface ParseErrorFactory {

  /**
   * The default error factory, allocating a {@code JsonParseError} with given message and the
   * traverser's contextual information.
   *
   * @see JsonParseError
   * @see JsonicErrorMessageFactory
   * @see JsonCharSourceTraverser#context()
   */
  ParseErrorFactory DEFAULT = (__, traverser, msg) -> {
    // Create simple JsonParseError using JsonicErrorMessageFactory under the hood
    return new JsonParseError(msg, traverser.context());
  };

  JsonParseError create(JsonSyntaxReader syntaxReader,
                        JsonCharSourceTraverser traverser,
                        String message);

}
