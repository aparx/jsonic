package io.github.aparx.jsonic.core.parser.context.tokens;

import io.github.aparx.jsonic.core.parser.context.JsonParseContextFactory;
import io.github.aparx.jsonic.core.parser.error.JsonParseError;
import io.github.aparx.jsonic.core.parser.tokens.JsonStringParser;
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
  public void testParse_WrongSyntaxThrowsErrors() {
    Assert.assertThrows(JsonParseError.class, () -> parse("Hello"));
    Assert.assertThrows(JsonParseError.class, () -> parse("\"Hello"));
    Assert.assertThrows(JsonParseError.class, () -> parse("Hello\""));
    Assert.assertThrows(JsonParseError.class, () -> parse(" \"Hello\""));
    Assert.assertThrows(NoSuchElementException.class, () -> parse("\""));
  }

  @Test
  public void testParse_RightSyntaxReturnsRightResults() {
    Assert.assertEquals("Hello", parse("\"Hello\""));
    Assert.assertEquals("foo bar baz", parse("\"foo bar baz\""));
    Assert.assertEquals("", parse("\"\""));
  }

  @Test
  public void testParse_AllowQuoteEscapeAndNewlines() {
    Assert.assertThrows(JsonParseError.class, () -> parse("\"Hello\\\""));
    Assert.assertThrows(JsonParseError.class, () -> parse("\\\"Hello\""));
    Assert.assertThrows(JsonParseError.class, () -> parse("\"He\\\"llo\\\""));
    Assert.assertEquals("the name is \"cool\"", parse("\"the name is \\\"cool\\\"\""));
    Assert.assertEquals("a new \n newline", parse("\"a new \n newline\""));
    Assert.assertEquals("\t\n", parse("\"\t\n\""));
  }

  @Test
  public void testParse_EnsureEarlyReturn() {
    Assert.assertEquals("this is some \"cool\" ", parse("\"this is some \\\"cool\\\" \"shizz\""));
  }

  @Test
  @SuppressWarnings("DataFlowIssue")
  public void testParse_EmptyThrowsError() {
    Assert.assertThrows(RuntimeException.class, () -> parse(""));
    Assert.assertThrows(RuntimeException.class, () -> parse("\u0000"));
    Assert.assertThrows(RuntimeException.class, () -> parse(null));
  }

  private String parse(CharSequence sequence) {
    return stringParser.parse(JsonParseContextFactory.read(sequence));
  }

}
