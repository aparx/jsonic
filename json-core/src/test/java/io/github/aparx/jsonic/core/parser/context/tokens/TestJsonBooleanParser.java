package io.github.aparx.jsonic.core.parser.context.tokens;

import io.github.aparx.jsonic.core.parser.JsonParser;
import io.github.aparx.jsonic.core.parser.error.JsonParseError;
import io.github.aparx.jsonic.core.parser.tokens.JsonBooleanParser;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author aparx (Vinzent Z.)
 * @version 2024-11-18 01:39
 * @since 1.0
 */
public class TestJsonBooleanParser {

  private final JsonBooleanParser parser = new JsonBooleanParser();

  @Test
  public void testParse_WrongSyntaxThrowsError() {
    Assert.assertThrows(JsonParseError.class, () -> JsonParser.parse(parser, "TRUE"));
    Assert.assertThrows(JsonParseError.class, () -> JsonParser.parse(parser, "tRue"));
    Assert.assertThrows(JsonParseError.class, () -> JsonParser.parse(parser, "tRUe"));
    Assert.assertThrows(JsonParseError.class, () -> JsonParser.parse(parser, "trUE"));
    Assert.assertThrows(JsonParseError.class, () -> JsonParser.parse(parser, "truE"));
    Assert.assertThrows(JsonParseError.class, () -> JsonParser.parse(parser, "True"));
    Assert.assertThrows(JsonParseError.class, () -> JsonParser.parse(parser, " true"));
    Assert.assertThrows(JsonParseError.class, () -> JsonParser.parse(parser, "\ttrue"));
    Assert.assertThrows(JsonParseError.class, () -> JsonParser.parse(parser, "\r\ntrue"));
    Assert.assertThrows(JsonParseError.class, () -> JsonParser.parse(parser, "\u0000true"));

    Assert.assertThrows(JsonParseError.class, () -> JsonParser.parse(parser, "FALSE"));
    Assert.assertThrows(JsonParseError.class, () -> JsonParser.parse(parser, "fAlse"));
    Assert.assertThrows(JsonParseError.class, () -> JsonParser.parse(parser, "fALse"));
    Assert.assertThrows(JsonParseError.class, () -> JsonParser.parse(parser, "falSE"));
    Assert.assertThrows(JsonParseError.class, () -> JsonParser.parse(parser, "falsE"));
    Assert.assertThrows(JsonParseError.class, () -> JsonParser.parse(parser, "False"));
    Assert.assertThrows(JsonParseError.class, () -> JsonParser.parse(parser, " false"));
    Assert.assertThrows(JsonParseError.class, () -> JsonParser.parse(parser, "\tfalse"));
    Assert.assertThrows(JsonParseError.class, () -> JsonParser.parse(parser, "\r\nfalse"));
    Assert.assertThrows(JsonParseError.class, () -> JsonParser.parse(parser, "\u0000false"));
  }

  @Test
  public void testParse_RightSyntaxReturnsRightResults() {
    Assert.assertEquals(true, JsonParser.parse(parser, "true"));
    Assert.assertEquals(false, JsonParser.parse(parser, "false"));
  }

  @Test
  public void testParse_EnsureEarlyReturn() {
    Assert.assertEquals(false, JsonParser.parse(parser, "false\t"));
    Assert.assertEquals(true, JsonParser.parse(parser, "true\t"));
    Assert.assertEquals(true, JsonParser.parse(parser, "trueSOME_OTHER_THINGS"));
    Assert.assertEquals(false, JsonParser.parse(parser, "falseSOME_OTHER_THINGS"));
  }

  @Test
  @SuppressWarnings("DataFlowIssue")
  public void testParse_EmptyThrowsError() {
    Assert.assertThrows(RuntimeException.class, () -> JsonParser.parse(parser, ""));
    Assert.assertThrows(RuntimeException.class, () -> JsonParser.parse(parser, "\u0000"));
    Assert.assertThrows(RuntimeException.class, () -> JsonParser.parse(parser, (String) null));
  }

}
