package io.github.aparx.jsonic.core.parser.tokens;

import io.github.aparx.jsonic.core.JsonSymbol;
import io.github.aparx.jsonic.core.parser.ComposableJsonParser;
import io.github.aparx.jsonic.core.parser.JsonParser;
import io.github.aparx.jsonic.core.parser.context.JsonParseContext;
import io.github.aparx.jsonic.core.parser.syntax.JsonSyntaxReader;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.com.google.common.base.Preconditions;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.Collection;
import java.util.function.Supplier;

/**
 * @author aparx (Vinzent Z.)
 * @version 2024-11-15 15:38
 * @since 1.0
 */
@DefaultQualifier(NonNull.class)
public class JsonArrayParser<E, T extends Collection<@Nullable E>> implements ComposableJsonParser<T> {

  private final Supplier<? extends T> collectionFactory;
  private final JsonParser<? extends E> elementParser;

  public JsonArrayParser(Supplier<? extends T> collectionFactory,
                         JsonParser<? extends E> elementParser) {
    Preconditions.checkNotNull(collectionFactory, "Collection factory must not be null");
    Preconditions.checkNotNull(elementParser, "Element parser must not be null");
    this.collectionFactory = collectionFactory;
    this.elementParser = elementParser;
  }

  @Override
  public T parse(JsonParseContext context) {
    @Nullable T col = this.collectionFactory.get();
    JsonSyntaxReader syntaxReader = context.syntaxReader();
    syntaxReader.expectSymbol(context, JsonSymbol.SQUARE_OPEN);
    boolean hasInserted = false;
    while (true) {
      syntaxReader.nextAndSkip(context, Character::isWhitespace);
      if (JsonSymbol.SQUARE_CLOSE.matches(context.current()))
        break;
      if (hasInserted) {
        syntaxReader.expectSymbol(context, JsonSymbol.COMMA);
        syntaxReader.nextAndSkip(context, Character::isWhitespace);
      }
      col.add(elementParser.parse(context));
      hasInserted = true;
    }
    syntaxReader.expectSymbol(context, JsonSymbol.SQUARE_CLOSE);
    return col;
  }

  @Override
  public boolean couldParse(char currentChar, int nextChar) {
    return currentChar == JsonSymbol.SQUARE_OPEN.literal();
  }
}