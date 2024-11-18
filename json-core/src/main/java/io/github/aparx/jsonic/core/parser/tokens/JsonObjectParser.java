package io.github.aparx.jsonic.core.parser.tokens;

import io.github.aparx.jsonic.core.JsonSymbol;
import io.github.aparx.jsonic.core.parser.ComposableJsonParser;
import io.github.aparx.jsonic.core.parser.JsonParser;
import io.github.aparx.jsonic.core.parser.error.ParseErrorFactory;
import io.github.aparx.jsonic.core.parser.context.JsonParseContext;
import io.github.aparx.jsonic.core.parser.syntax.JsonSyntaxReader;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.com.google.common.base.Preconditions;
import org.checkerframework.com.google.errorprone.annotations.CanIgnoreReturnValue;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.Map;
import java.util.function.Supplier;

/**
 * @author aparx (Vinzent Z.)
 * @version 2024-11-15 15:41
 * @since 1.0
 */
@DefaultQualifier(NonNull.class)
public class JsonObjectParser<K, V> implements ComposableJsonParser<Map<@Nullable K, @Nullable V>> {

  private static final String DUPLICATE_KEY_ERROR = "Key %s is a duplicate";

  private final Supplier<Map<@Nullable K, @Nullable V>> mapFactory;
  private final JsonParser<? extends K> keyParser;
  private final JsonParser<? extends V> valueParser;

  /** Allows no duplicate keys */
  private boolean strict;

  public JsonObjectParser(Supplier<Map<K, V>> mapFactory,
                          JsonParser<? extends K> keyParser,
                          JsonParser<? extends V> valueParser) {
    Preconditions.checkNotNull(mapFactory, "Map factory must not be null");
    Preconditions.checkNotNull(keyParser, "Key parser must not be null");
    Preconditions.checkNotNull(valueParser, "Value parser must not be null");
    this.mapFactory = mapFactory;
    this.keyParser = keyParser;
    this.valueParser = valueParser;
  }

  @Override
  public Map<@Nullable K, @Nullable V> parse(JsonParseContext context) {
    Map<@Nullable K, @Nullable V> map = this.mapFactory.get();
    JsonSyntaxReader syntaxReader = context.syntaxReader();
    ParseErrorFactory errorHandler = syntaxReader.errorFactory();
    syntaxReader.expectSymbol(context, JsonSymbol.CURLY_OPEN);
    while (context.hasNext()) {
      syntaxReader.readAndSkip(context, Character::isWhitespace);
      if (JsonSymbol.CURLY_CLOSE.matches(context.current())) break;
      if (!map.isEmpty()) {
        // Handle separation of multiple KV-pairs
        syntaxReader.expectSymbol(context, JsonSymbol.COMMA);
        syntaxReader.readAndSkip(context, Character::isWhitespace);
      }
      @Nullable K key = this.keyParser.parse(context);
      if (this.strict && map.containsKey(key))
        throw errorHandler.create(syntaxReader, context,
            String.format(DUPLICATE_KEY_ERROR, key));
      syntaxReader.readAndSkip(context, Character::isWhitespace);
      syntaxReader.expectSymbol(context, JsonSymbol.COLON);
      syntaxReader.readAndSkip(context, Character::isWhitespace);
      map.put(key, this.valueParser.parse(context));
    }
    syntaxReader.readAndSkip(context, Character::isWhitespace);
    syntaxReader.expectSymbol(context, JsonSymbol.CURLY_CLOSE);
    return map;
  }

  @Override
  public boolean couldParse(char currentChar, int nextChar) {
    return currentChar == JsonSymbol.CURLY_OPEN.literal();
  }

  @CanIgnoreReturnValue
  public JsonObjectParser<K, V> setStrict(boolean strict) {
    this.strict = strict;
    return this;
  }

  public boolean strict() {
    return strict;
  }
}