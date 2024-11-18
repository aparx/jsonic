package io.github.aparx.jsonic.core.parser.context.tokens;

import io.github.aparx.jsonic.core.parser.JsonParser;
import io.github.aparx.jsonic.core.parser.source.JsonCharSourceTraverserFactory;
import io.github.aparx.jsonic.core.parser.error.JsonParseError;
import io.github.aparx.jsonic.core.parser.syntax.DefaultJsonSyntaxReader;
import io.github.aparx.jsonic.core.parser.syntax.JsonSyntaxReader;
import io.github.aparx.jsonic.core.parser.tokens.JsonNullParser;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author aparx (Vinzent Z.)
 * @version 2024-11-18 21:55
 * @since 1.0
 */
public class TestJsonNullParser {

  private final JsonNullParser parser = JsonNullParser.DEFAULT;

  @Test
  public void testParse_WrongSyntaxThrowsError() {
    Assert.assertThrows(JsonParseError.class, () -> JsonParser.parse(parser, "nil"));
    Assert.assertThrows(JsonParseError.class, () -> JsonParser.parse(parser, "NULL"));
    Assert.assertThrows(JsonParseError.class, () -> JsonParser.parse(parser, "nul"));
    Assert.assertThrows(JsonParseError.class, () -> JsonParser.parse(parser, "nULl"));
    Assert.assertThrows(JsonParseError.class, () -> JsonParser.parse(parser, "nUll"));
    Assert.assertThrows(JsonParseError.class, () -> JsonParser.parse(parser, "nuLL"));
    Assert.assertThrows(JsonParseError.class, () -> JsonParser.parse(parser, "nulL"));
    Assert.assertThrows(JsonParseError.class, () -> JsonParser.parse(parser, "Null"));
    Assert.assertThrows(JsonParseError.class, () -> JsonParser.parse(parser, "\tnull"));
    Assert.assertThrows(JsonParseError.class, () -> JsonParser.parse(parser, "\r\nnull"));
    Assert.assertThrows(JsonParseError.class, () -> JsonParser.parse(parser, "\u0000null"));
  }

  @Test
  public void testParse_RightSyntaxReturnsRightResult() {
    Assert.assertNull(JsonParser.parse(parser, "null"));
  }

  @Test
  public void testParse_EnsureEarlyReturn() {
    Assert.assertNull(JsonParser.parse(parser, "null\t"));
    Assert.assertNull(JsonParser.parse(parser, "null\t"));
    Assert.assertNull(JsonParser.parse(parser, "nullSOME_OTHER_THINGS"));
    Assert.assertNull(JsonParser.parse(parser, "nullSOME_OTHER_THINGS"));
  }

  @Test
  @SuppressWarnings("DataFlowIssue")
  public void testParse_EmptyThrowsError() {
    Assert.assertThrows(RuntimeException.class, () -> JsonParser.parse(parser, ""));
    Assert.assertThrows(RuntimeException.class, () -> JsonParser.parse(parser, "\u0000"));
    Assert.assertThrows(RuntimeException.class, () -> JsonParser.parse(parser, (String) null));
  }


}
