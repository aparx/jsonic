package io.github.aparx.jsonic.core.parser.context.tokens;

import io.github.aparx.jsonic.core.parser.JsonParser;
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

  private final JsonStringParser parser = new JsonStringParser();

  @Test
  public void testParse_WrongSyntaxThrowsErrors() {
    Assert.assertThrows(JsonParseError.class, () -> JsonParser.parse(parser, "Hello"));
    Assert.assertThrows(JsonParseError.class, () -> JsonParser.parse(parser, "\"Hello"));
    Assert.assertThrows(JsonParseError.class, () -> JsonParser.parse(parser, "Hello\""));
    Assert.assertThrows(JsonParseError.class, () -> JsonParser.parse(parser, " \"Hello\""));
    Assert.assertThrows(NoSuchElementException.class, () -> JsonParser.parse(parser, "\""));
  }

  @Test
  public void testParse_RightSyntaxReturnsRightResults() {
    Assert.assertEquals("Hello", JsonParser.parse(parser, "\"Hello\""));
    Assert.assertEquals("foo bar baz", JsonParser.parse(parser, "\"foo bar baz\""));
    Assert.assertEquals("", JsonParser.parse(parser, "\"\""));
  }

  @Test
  public void testParse_AllowQuoteEscapeAndNewlines() {
    Assert.assertThrows(JsonParseError.class, () -> JsonParser.parse(parser, "\"Hello\\\""));
    Assert.assertThrows(JsonParseError.class, () -> JsonParser.parse(parser, "\\\"Hello\""));
    Assert.assertThrows(JsonParseError.class, () -> JsonParser.parse(parser, "\"He\\\"llo\\\""));
    Assert.assertEquals("the name is \"cool\"",
        JsonParser.parse(parser, "\"the name is \\\"cool\\\"\""));
    Assert.assertEquals("a new \n newline", JsonParser.parse(parser, "\"a new \n newline\""));
    Assert.assertEquals("\t\n", JsonParser.parse(parser, "\"\t\n\""));
  }

  @Test
  public void testParse_EnsureEarlyReturn() {
    Assert.assertEquals("this is some \"cool\" ",
        JsonParser.parse(parser, "\"this is some \\\"cool\\\" \"shizz\""));
  }

  @Test
  @SuppressWarnings("DataFlowIssue")
  public void testParse_EmptyThrowsError() {
    Assert.assertThrows(RuntimeException.class, () -> JsonParser.parse(parser, ""));
    Assert.assertThrows(RuntimeException.class, () -> JsonParser.parse(parser, "\u0000"));
    Assert.assertThrows(RuntimeException.class, () -> JsonParser.parse(parser, (String) null));
  }


}
