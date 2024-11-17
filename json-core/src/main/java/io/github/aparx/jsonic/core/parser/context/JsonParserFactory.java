package io.github.aparx.jsonic.core.parser.context;

import io.github.aparx.jsonic.core.parser.JsonParser;
import io.github.aparx.jsonic.core.parser.context.tokens.*;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.com.google.errorprone.annotations.CheckReturnValue;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.*;
import java.util.function.Supplier;

/**
 * @author aparx (Vinzent Z.)
 * @version 2024-11-15 16:42
 * @since 1.0
 */
@DefaultQualifier(NonNull.class)
public final class JsonParserFactory {

  private static final Supplier<Map<?, ?>> RECORD_UNORDERED_MAP = HashMap::new;
  private static final Supplier<Map<?, ?>> RECORD_ORDERED_MAP = LinkedHashMap::new;

  private static final Supplier<List<?>> ARRAY_DEFAULT_LIST = ArrayList::new;
  private static final Supplier<Set<?>> ARRAY_SET_LIST = HashSet::new;

  private JsonParserFactory() {
    throw new AssertionError();
  }

  // <====================> OTHERS <====================>

  public static JsonNullParser nil() {
    return JsonNullParser.DEFAULT;
  }

  public static JsonBooleanParser bool() {
    return JsonBooleanParser.DEFAULT;
  }

  public static JsonStringParser string() {
    return JsonStringParser.DEFAULT;
  }

  // <====================> ARRAY/SET <====================>

  public static <E, T extends Collection<@Nullable E>> JsonArrayParser<E, T>
  array(Supplier<? extends T> collectionFactory, JsonParser<? extends E> elementParser) {
    return new JsonArrayParser<>(collectionFactory, elementParser);
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  public static <E> JsonArrayParser<E, List<@Nullable E>> array(JsonParser<? extends E> elementParser) {
    return new JsonArrayParser<>((Supplier) ARRAY_DEFAULT_LIST, elementParser);
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  public static <E> JsonArrayParser<E, Set<@Nullable E>>
  hashSet(JsonParser<? extends E> elementParser) {
    return new JsonArrayParser<>((Supplier) ARRAY_SET_LIST, elementParser);
  }

  public static JsonArrayParser<String, List<String>> stringArray() {
    return array(JsonStringParser.DEFAULT);
  }

  public static JsonArrayParser<Boolean, List<Boolean>> boolArray() {
    return array(JsonBooleanParser.DEFAULT);
  }

  // <====================> RECORD <====================>

  @CheckReturnValue
  public static <K, V> JsonRecordParser<K, V> record(
      Supplier<Map<K, V>> mapFactory,
      JsonParser<? extends K> keyParser,
      JsonParser<? extends V> valueParser) {
    return new JsonRecordParser<>(mapFactory, keyParser, valueParser);
  }

  @CheckReturnValue
  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <@Nullable K, @Nullable V> JsonRecordParser<K, V> record(
      JsonParser<@Nullable ? extends K> keyParser,
      JsonParser<@Nullable ? extends V> valueParser) {
    return new JsonRecordParser<>((Supplier) RECORD_UNORDERED_MAP, keyParser, valueParser);
  }

  @CheckReturnValue
  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <@Nullable K, @Nullable V> JsonRecordParser<K, V> orderedRecord(
      JsonParser<@Nullable ? extends K> keyParser,
      JsonParser<@Nullable ? extends V> valueParser) {
    return new JsonRecordParser<>((Supplier) RECORD_ORDERED_MAP, keyParser, valueParser);
  }

}
