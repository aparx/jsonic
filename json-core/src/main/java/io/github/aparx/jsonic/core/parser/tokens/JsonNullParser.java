package io.github.aparx.jsonic.core.parser.tokens;

import io.github.aparx.jsonic.core.parser.ComposableJsonParser;
import io.github.aparx.jsonic.core.parser.source.JsonCharSourceTraverser;
import io.github.aparx.jsonic.core.parser.syntax.JsonSyntaxReader;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import javax.annotation.Nullable;

/**
 * @author aparx (Vinzent Z.)
 * @version 2024-11-16 11:05
 * @since 1.0
 */
@DefaultQualifier(NonNull.class)
public class JsonNullParser implements ComposableJsonParser<Object> {

  public static final JsonNullParser DEFAULT = new JsonNullParser();

  @Override
  public @Nullable Object parse(JsonCharSourceTraverser traverser, JsonSyntaxReader syntaxReader) {
    syntaxReader.expectLiteral(traverser, "null");
    return null;
  }

  @Override
  public boolean couldParse(char currentChar, int nextChar) {
    return currentChar == 'n' && nextChar >= 0 && ((char) nextChar) == 'u';
  }
}
