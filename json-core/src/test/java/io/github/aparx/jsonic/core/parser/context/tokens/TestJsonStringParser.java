package io.github.aparx.jsonic.core.parser.context.tokens;

import io.github.aparx.jsonic.core.parser.context.JsonParseContextFactory;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.junit.Assert;
import org.junit.Test;

import java.util.NoSuchElementException;

/**
 * @author aparx (Vinzent Z.)
 * @version 2024-11-16 11:27
 * @since 1.0
 */
@DefaultQualifier(NonNull.class)
public class TestJsonStringParser {

  private final JsonStringParser stringParser = new JsonStringParser();

  @Test
  public void testParseSyntaxIsEnforced() {
    Assert.assertThrows(RuntimeException.class, () -> parse("Hello"));
    Assert.assertThrows(RuntimeException.class, () -> parse("\"Hello"));
    Assert.assertThrows(RuntimeException.class, () -> parse("Hello\""));
    Assert.assertThrows(RuntimeException.class, () -> parse(" \"Hello\""));
    Assert.assertThrows(NoSuchElementException.class, () -> parse("\""));
    Assert.assertEquals("Hello", parse("\"Hello\""));
    Assert.assertEquals("foo bar baz", parse("\"foo bar baz\""));
    Assert.assertEquals("", parse("\"\""));
  }

  @Test
  public void testParseAllowQuoteEscapeAndNewlines() {
    Assert.assertThrows(RuntimeException.class, () -> parse("\"Hello\\\""));
    Assert.assertThrows(RuntimeException.class, () -> parse("\\\"Hello\""));
    Assert.assertThrows(RuntimeException.class, () -> parse("\"He\\\"llo\\\""));
    Assert.assertEquals("the name is \"cool\"", parse("\"the name is \\\"cool\\\"\""));
    Assert.assertEquals("a new \n newline", parse("\"a new \n newline\""));
    Assert.assertEquals("\t\n", parse("\"\t\n\""));
  }

  @Test
  public void testParseEnsureEarlyReturn() {
    Assert.assertEquals("this is some \"cool\" ", parse("\"this is some \\\"cool\\\" \"shizz\""));
  }

  private String parse(CharSequence sequence) {
    return stringParser.parse(JsonParseContextFactory.of(sequence));
  }

}
