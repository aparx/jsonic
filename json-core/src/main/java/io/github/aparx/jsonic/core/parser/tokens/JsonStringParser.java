package io.github.aparx.jsonic.core.parser.tokens;

import io.github.aparx.jsonic.core.JsonSymbol;
import io.github.aparx.jsonic.core.parser.ComposableJsonParser;
import io.github.aparx.jsonic.core.parser.error.ParseErrorFactory;
import io.github.aparx.jsonic.core.parser.source.JsonCharSourceTraverser;
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
  public String parse(JsonCharSourceTraverser traverser, JsonSyntaxReader syntaxReader) {
    ParseErrorFactory errorHandler = syntaxReader.errorFactory();
    syntaxReader.expectSymbol(traverser, JsonSymbol.DOUBLE_QUOTE);
    StringBuilder builder = new StringBuilder();
    char lastChar = traverser.current();
    do {
      char nextChar = traverser.next();
      if (JsonSymbol.DOUBLE_QUOTE.matches(nextChar)
          && (lastChar != QUOTE_ESCAPE || !traverser.hasNext()))
        break;
      lastChar = nextChar;
      if (nextChar == QUOTE_ESCAPE && JsonSymbol.DOUBLE_QUOTE.matches(traverser.peek()))
        continue; // Omit the escaping character from plaintext
      builder.append(nextChar);
    } while (traverser.hasNext());
    if (lastChar == QUOTE_ESCAPE)
      throw errorHandler.create(syntaxReader, traverser, "Last double quote is escaped");
    syntaxReader.expectSymbol(traverser, JsonSymbol.DOUBLE_QUOTE);
    return builder.toString();
  }

  @Override
  public boolean couldParse(char currentChar, int nextChar) {
    return currentChar == JsonSymbol.DOUBLE_QUOTE.literal();
  }
}