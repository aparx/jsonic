package io.github.aparx.jsonic.core.parser.context;

import io.github.aparx.jsonic.core.JsonSymbol;
import io.github.aparx.jsonic.core.parser.ParseErrorFactory;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.com.google.errorprone.annotations.CanIgnoreReturnValue;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.checkerframework.org.apache.commons.text.CharacterPredicate;

/**
 * @author aparx (Vinzent Z.)
 * @version 2024-11-15 14:25
 * @since 1.0
 */
@DefaultQualifier(NonNull.class)
public interface JsonSyntaxReader {

  ParseErrorFactory errorHandler();

  @CanIgnoreReturnValue
  char readNextAndSkip(JsonParseContext context, CharacterPredicate skipPredicate);

  void expectSymbol(JsonParseContext context, JsonSymbol symbol);

  void expectLiteral(JsonParseContext context, String literal);

  void expectLiteral(JsonParseContext context, char literal);

}
