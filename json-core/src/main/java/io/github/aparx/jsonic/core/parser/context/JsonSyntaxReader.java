package io.github.aparx.jsonic.core.parser.context;

import io.github.aparx.jsonic.core.JsonSymbol;
import io.github.aparx.jsonic.core.parser.ParseErrorFactory;
import org.checkerframework.checker.nullness.qual.NonNull;
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

  void readAndSkip(JsonParseContext context, CharacterPredicate skipPredicate);

  /**
   * Expects a symbol at current character from {@code context}
   *
   * @param context
   * @param symbol
   */
  void expectSymbol(JsonParseContext context, JsonSymbol symbol);

  void expectLiteral(JsonParseContext context, String literal);

  void expectLiteral(JsonParseContext context, char literal);

}