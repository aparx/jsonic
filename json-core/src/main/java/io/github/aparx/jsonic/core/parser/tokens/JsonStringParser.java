package io.github.aparx.jsonic.core.parser.tokens;

import io.github.aparx.jsonic.core.JsonSymbol;
import io.github.aparx.jsonic.core.parser.ComposableJsonParser;
import io.github.aparx.jsonic.core.parser.error.ParseErrorFactory;
import io.github.aparx.jsonic.core.parser.context.JsonParseContext;
import io.github.aparx.jsonic.core.parser.syntax.JsonSyntaxReader;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

/**
 * @author aparx (Vinzent Z.)
 * @version 2024-11-15 15:14
 * @since 1.0
 */
@DefaultQualifier(NonNull.class)
public class JsonStringParser implements ComposableJsonParser<String> {

  public static final JsonStringParser DEFAULT = new JsonStringParser();

  public static final char QUOTE_ESCAPE = '\\';

  @Override
  public String parse(JsonParseContext context) {
    JsonSyntaxReader syntaxReader = context.syntaxReader();
    ParseErrorFactory errorHandler = syntaxReader.errorFactory();
    syntaxReader.expectSymbol(context, JsonSymbol.DOUBLE_QUOTE);
    StringBuilder builder = new StringBuilder();
    char lastChar = context.current();
    do {
      char nextChar = context.next();
      if (JsonSymbol.DOUBLE_QUOTE.matches(nextChar)
          && (lastChar != QUOTE_ESCAPE || !context.hasNext()))
        break;
      lastChar = nextChar;
      if (nextChar == QUOTE_ESCAPE && JsonSymbol.DOUBLE_QUOTE.matches(context.peek()))
        continue; // Omit the escaping character from plaintext
      builder.append(nextChar);
    } while (context.hasNext());
    if (lastChar == QUOTE_ESCAPE)
      throw errorHandler.create(syntaxReader, context, "Last double quote is escaped");
    syntaxReader.expectSymbol(context, JsonSymbol.DOUBLE_QUOTE);
    return builder.toString();
  }

  @Override
  public boolean couldParse(char currentChar, int nextChar) {
    return currentChar == JsonSymbol.DOUBLE_QUOTE.literal();
  }
}