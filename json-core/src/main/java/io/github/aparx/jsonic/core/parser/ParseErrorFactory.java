package io.github.aparx.jsonic.core.parser;

import io.github.aparx.jsonic.core.parser.context.JsonParseContext;
import io.github.aparx.jsonic.core.parser.context.JsonSyntaxReader;
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

  RuntimeException createError(JsonSyntaxReader syntaxReader, JsonParseContext context,
                               String message);

}
