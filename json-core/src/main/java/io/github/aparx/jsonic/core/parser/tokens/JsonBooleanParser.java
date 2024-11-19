package io.github.aparx.jsonic.core.parser.tokens;

import io.github.aparx.jsonic.core.parser.ComposableJsonParser;
import io.github.aparx.jsonic.core.parser.source.JsonCharSourceTraverser;
import io.github.aparx.jsonic.core.parser.syntax.JsonSyntaxReader;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

/**
 * @author aparx (Vinzent Z.)
 * @version 2024-11-15 15:49
 * @since 1.0
 */
@DefaultQualifier(NonNull.class)
public class JsonBooleanParser implements ComposableJsonParser<Boolean> {

  public static final JsonBooleanParser DEFAULT = new JsonBooleanParser();

  private static final String ERROR_UNEXPECTED_TOKEN =
      "Unexpected token: %s expected: 'true' or 'false'";

  @Override
  public Boolean parse(JsonCharSourceTraverser traverser, JsonSyntaxReader syntaxReader) {
    return switch (traverser.current()) {
      case 't' -> {
        syntaxReader.expectLiteral(traverser, "true");
        yield true;
      }
      case 'f' -> {
        syntaxReader.expectLiteral(traverser, "false");
        yield false;
      }
      default -> throw syntaxReader.errorFactory().create(syntaxReader, traverser,
          String.format(ERROR_UNEXPECTED_TOKEN, traverser.current()));
    };
  }

  @Override
  public boolean couldParse(char currentChar, int nextChar) {
    return currentChar == 't' || currentChar == 'f';
  }
}
