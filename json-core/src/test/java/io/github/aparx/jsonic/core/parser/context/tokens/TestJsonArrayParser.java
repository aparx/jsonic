package io.github.aparx.jsonic.core.parser.context.tokens;

import io.github.aparx.jsonic.core.parser.context.JsonParseContextFactory;
import io.github.aparx.jsonic.core.parser.JsonParserFactory;
import io.github.aparx.jsonic.core.parser.error.JsonParseError;
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

  private final JsonArrayParser<String, List<String>> stringParser =
      JsonParserFactory.stringArray();

  private final JsonArrayParser<String, List<String>> simpleParser =
      JsonParserFactory.array((ctx) -> {
        // The elements of this array should only be considered valid when: ['z'>=x>='a']
        // Optionally, one can ensure that the returning string is not empty, to disallow invalid
        // syntax, which would result in parse("[A]") throwing the error thrown in here.
        return ctx.syntaxReader().accumulate(ctx, (__, x) -> x >= 'a' && x <= 'z');
      });

  @Test
  public void testParse_WrongSyntaxThrowsErrors() {
    Assert.assertThrows(JsonParseError.class, () -> parse(this.simpleParser, "[foo"));
    Assert.assertThrows(JsonParseError.class, () -> parse(this.simpleParser, "foo]"));
    Assert.assertThrows(JsonParseError.class, () -> parse(this.simpleParser, " [foo]"));
    Assert.assertThrows(JsonParseError.class, () -> parse(this.simpleParser, "[foo bar]"));
    Assert.assertThrows(JsonParseError.class, () -> parse(this.simpleParser, "[foo bar]"));
    Assert.assertThrows(JsonParseError.class, () -> parse(this.simpleParser, "[foo, bar"));
    Assert.assertThrows(JsonParseError.class, () -> parse(this.simpleParser, "foo, bar]"));
  }

  @Test
  public void testParse_RightSyntaxReturnsRightResults() {
    Assert.assertEquals(List.of(), parse(this.simpleParser, "[]"));
    Assert.assertEquals(List.of("a"), parse(this.simpleParser, "[a]"));
    Assert.assertEquals(List.of("foo"), parse(this.simpleParser, "[foo]"));
    Assert.assertEquals(List.of("foo", "bar"), parse(this.simpleParser, "[foo, bar]"));
    Assert.assertEquals(List.of("foo", "bar", ""), parse(this.simpleParser, "[foo, bar,]"));
    Assert.assertEquals(List.of("foo", "bar", "baz"), parse(this.simpleParser, "[foo, bar, baz]"));
  }

  @Test
  public void testParseStringArray_WrongSyntaxThrowsError() {
    Assert.assertThrows(JsonParseError.class, () -> parse(stringParser, "["));
    Assert.assertThrows(JsonParseError.class, () -> parse(stringParser, "]"));
    Assert.assertThrows(JsonParseError.class, () -> parse(stringParser, "[\"]"));
    Assert.assertThrows(JsonParseError.class, () -> parse(stringParser, "[\"\\\"]"));
    Assert.assertThrows(JsonParseError.class, () -> parse(stringParser, "[\"hello]"));
    Assert.assertThrows(JsonParseError.class, () -> parse(stringParser, "[world\"]"));
    Assert.assertThrows(JsonParseError.class, () -> parse(stringParser, "[\\\"world\"]"));
  }

  @Test
  public void testParseStringArray_RightSyntaxReturnsRightResults() {
    Assert.assertEquals(List.of(), parse(stringParser, "[]"));
    Assert.assertEquals(List.of(""), parse(stringParser, "[\"\"]"));
    Assert.assertEquals(List.of("a"), parse(stringParser, "[\"a\"]"));
    Assert.assertEquals(List.of("hello"), parse(stringParser, "[\"hello\"]"));
    Assert.assertEquals(List.of("a", "b"), parse(stringParser, "[\"a\", \"b\"]"));
    Assert.assertEquals(List.of("hello", "world", "!"),
        parse(stringParser, "[\"hello\", \"world\", \"!\"]"));
  }

  @Test
  public void testLargerRandomArraysToBeParsedCorrectly() {
    String simpleAlphabet = "abcdefghijklmnopqrstuvwxyz";
    String complexAlphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_.,:;" +
        " \t";
    for (int i = 0; i < 50 /* RANDOM_ARRAY_ITERATIONS */; ++i) {
      parseRandomArray(simpleParser, simpleAlphabet, Function.identity());
      parseRandomArray(stringParser, complexAlphabet, (x) -> String.format("\"%s\"", x));
    }
  }

  @Test
  @SuppressWarnings("DataFlowIssue")
  public void testParse_EmptyThrowsError() {
    Assert.assertThrows(RuntimeException.class, () -> parse(simpleParser, ""));
    Assert.assertThrows(RuntimeException.class, () -> parse(simpleParser, "\u0000"));
    Assert.assertThrows(RuntimeException.class, () -> parse(simpleParser, null));
  }

  private void parseRandomArray(JsonArrayParser<String, List<String>> parser,
                                String charPool, Function<String, String> joinMapper) {
    String[] strings = generateRandomArray(charPool);
    String joint = Arrays.stream(strings).map(joinMapper).collect(Collectors.joining(", "));
    Assert.assertEquals(List.of(strings), this.parse(parser, '[' + joint + ']'));
    Assert.assertThrows(JsonParseError.class, () -> this.parse(parser, '[' + joint));
    Assert.assertThrows(JsonParseError.class, () -> this.parse(parser, joint + ']'));
  }

  private List<String> parse(JsonArrayParser<String, List<String>> parser, CharSequence sequence) {
    return parser.parse(JsonParseContextFactory.read(sequence));
  }

  private String[] generateRandomArray(String charPool) {
    String[] array = new String[GEN_RANDOM.nextInt(0, 100)];
    for (int i = 0; i < array.length; ++i)
      array[i] = generateString(GEN_RANDOM.nextInt(5, 50), charPool);
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
