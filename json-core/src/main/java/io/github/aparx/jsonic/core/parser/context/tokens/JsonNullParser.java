package io.github.aparx.jsonic.core.parser.context.tokens;

import io.github.aparx.jsonic.core.parser.ComposableJsonParser;
import io.github.aparx.jsonic.core.parser.context.JsonParseContext;
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
  public @Nullable Object parse(JsonParseContext context) {
    context.syntaxReader().expectLiteral(context, "null");
    return null;
  }

  @Override
  public boolean couldParse(char currentChar, int nextChar) {
    return currentChar == 'n' && nextChar >= 0 && ((char) nextChar) == 'u';
  }
}
