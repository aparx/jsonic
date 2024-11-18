package io.github.aparx.jsonic.core.parser.context.tokens;

import io.github.aparx.jsonic.core.parser.context.JsonParseContextFactory;
import io.github.aparx.jsonic.core.parser.JsonParserFactory;
import io.github.aparx.jsonic.core.parser.tokens.JsonArrayParser;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.com.google.common.base.Preconditions;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author aparx (Vinzent Z.)
 * @version 2024-11-16 11:41
 * @since 1.0
 */
@DefaultQualifier(NonNull.class)
public class TestJsonArrayParser {

  private static final Random GEN_RANDOM = new Random(38656245L);

  private final JsonArrayParser<String, List<String>> strParser = JsonParserFactory.stringArray();

  private final JsonArrayParser<String, List<String>> parser = JsonParserFactory.array((ctx) -> {
    // The elements of this array should only be considered valid when: ['z'>=x>='a']
    // Optionally, one can ensure that the returning string is not empty, to disallow invalid
    // syntax, which would result in parse("[A]") throwing the error thrown in here.
    return ctx.syntaxReader().accumulate(ctx, (__, x) -> x >= 'a' && x <= 'z');
  });

  @Test
  public void testParse_WrongSyntaxThrowsErrors() {
    Assert.assertThrows(RuntimeException.class, () -> parse(this.parser, "[foo"));
    Assert.assertThrows(RuntimeException.class, () -> parse(this.parser, "foo]"));
    Assert.assertThrows(RuntimeException.class, () -> parse(this.parser, " [foo]"));
    Assert.assertThrows(RuntimeException.class, () -> parse(this.parser, "[foo bar]"));
    Assert.assertThrows(RuntimeException.class, () -> parse(this.parser, "[foo bar]"));
    Assert.assertThrows(RuntimeException.class, () -> parse(this.parser, "[foo, bar"));
    Assert.assertThrows(RuntimeException.class, () -> parse(this.parser, "foo, bar]"));
  }

  @Test
  public void testParse_RightSyntaxReturnsRightResults() {
    Assert.assertEquals(List.of(), parse(this.parser, "[]"));
    Assert.assertEquals(List.of("a"), parse(this.parser, "[a]"));
    Assert.assertEquals(List.of("foo"), parse(this.parser, "[foo]"));
    Assert.assertEquals(List.of("foo", "bar"), parse(this.parser, "[foo, bar]"));
    Assert.assertEquals(List.of("foo", "bar", ""), parse(this.parser, "[foo, bar,]"));
    Assert.assertEquals(List.of("foo", "bar", "baz"), parse(this.parser, "[foo, bar, baz]"));
  }

  @Test
  public void testParseStringArray_WrongSyntaxThrowsError() {
    Assert.assertThrows(RuntimeException.class, () -> parse(strParser, "["));
    Assert.assertThrows(RuntimeException.class, () -> parse(strParser, "]"));
    Assert.assertThrows(RuntimeException.class, () -> parse(strParser, "[\"]"));
    Assert.assertThrows(RuntimeException.class, () -> parse(strParser, "[\"\\\"]"));
    Assert.assertThrows(RuntimeException.class, () -> parse(strParser, "[\"hello]"));
    Assert.assertThrows(RuntimeException.class, () -> parse(strParser, "[world\"]"));
    Assert.assertThrows(RuntimeException.class, () -> parse(strParser, "[\\\"world\"]"));
  }

  @Test
  public void testParseStringArray_RightSyntaxReturnsRightResults() {
    Assert.assertEquals(List.of(), parse(strParser, "[]"));
    Assert.assertEquals(List.of(""), parse(strParser, "[\"\"]"));
    Assert.assertEquals(List.of("a"), parse(strParser, "[\"a\"]"));
    Assert.assertEquals(List.of("hello"), parse(strParser, "[\"hello\"]"));
    Assert.assertEquals(List.of("a", "b"), parse(strParser, "[\"a\", \"b\"]"));
    Assert.assertEquals(List.of("hello", "world", "!"),
        parse(strParser, "[\"hello\", \"world\", \"!\"]"));
  }

  @Test
  public void testLargerRandomArraysToBeParsedCorrectly() {
    String simpleAlphabet = "abcdefghijklmnopqrstuvwxyz";
    String complexAlphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_.,:;" +
        " \t";
    for (int i = 0; i < 50; ++i) {
      parseRandomArray(this.parser, simpleAlphabet, Function.identity());
      parseRandomArray(strParser, complexAlphabet, (x) -> String.format("\"%s\"", x));
    }
  }


  private List<String> parse(JsonArrayParser<String, List<String>> parser, CharSequence sequence) {
    return parser.parse(JsonParseContextFactory.read(sequence));
  }

  private void parseRandomArray(JsonArrayParser<String, List<String>> parser,
                                String pool, Function<String, String> joinMapper) {
    String[] strings = generateRandomArray(pool);
    String joint = Arrays.stream(strings).map(joinMapper).collect(Collectors.joining(", "));
    Assert.assertEquals(List.of(strings), this.parse(parser, '[' + joint + ']'));
    Assert.assertThrows(RuntimeException.class, () -> this.parse(parser, '[' + joint));
    Assert.assertThrows(RuntimeException.class, () -> this.parse(parser, joint + ']'));
  }

  private String[] generateRandomArray(String pool) {
    String[] array = new String[GEN_RANDOM.nextInt(0, 100)];
    for (int i = 0; i < array.length; ++i)
      array[i] = generateString(GEN_RANDOM.nextInt(5, 50), pool);
    return array;
  }

  private String generateString(int length, String pool) {
    Preconditions.checkArgument(length >= 0, "Length must be positive");
    int poolLength = pool.length();
    char[] array = new char[length];
    while (length-- > 0)
      array[length] = pool.charAt(GEN_RANDOM.nextInt(0, poolLength));
    return new String(array);
  }

}
