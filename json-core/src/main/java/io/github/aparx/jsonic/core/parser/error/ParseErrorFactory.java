package io.github.aparx.jsonic.core.parser.error;

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

  JsonParseError create(JsonSyntaxReader syntaxReader,
                        JsonCharSourceTraverser traverser,
                        String message);

  // TODO
  //  check if traverser's context is typeof HistoryAwareParseContext, if so can provide stacktrace

}
