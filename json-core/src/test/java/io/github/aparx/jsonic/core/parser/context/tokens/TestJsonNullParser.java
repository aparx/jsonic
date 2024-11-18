package io.github.aparx.jsonic.core.parser.context.tokens;

import io.github.aparx.jsonic.core.parser.context.JsonParseContextFactory;
import io.github.aparx.jsonic.core.parser.error.JsonParseError;
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
    Assert.assertThrows(JsonParseError.class, () -> parse("nil"));
    Assert.assertThrows(JsonParseError.class, () -> parse("NULL"));
    Assert.assertThrows(JsonParseError.class, () -> parse("nul"));
    Assert.assertThrows(JsonParseError.class, () -> parse("nULl"));
    Assert.assertThrows(JsonParseError.class, () -> parse("nUll"));
    Assert.assertThrows(JsonParseError.class, () -> parse("nuLL"));
    Assert.assertThrows(JsonParseError.class, () -> parse("nulL"));
    Assert.assertThrows(JsonParseError.class, () -> parse("Null"));
    Assert.assertThrows(JsonParseError.class, () -> parse("\tnull"));
    Assert.assertThrows(JsonParseError.class, () -> parse("\r\nnull"));
    Assert.assertThrows(JsonParseError.class, () -> parse("\u0000null"));
  }

  @Test
  public void testParse_RightSyntaxReturnsRightResult() {
    Assert.assertNull(parse("null"));
  }

  @Test
  public void testParse_EnsureEarlyReturn() {
    Assert.assertNull(parse("null\t"));
    Assert.assertNull(parse("null\t"));
    Assert.assertNull(parse("nullSOME_OTHER_THINGS"));
    Assert.assertNull(parse("nullSOME_OTHER_THINGS"));
  }

  @Test
  public void testParse_EmptyThrowsError() {
    Assert.assertThrows(RuntimeException.class, () -> parse(""));
    Assert.assertThrows(RuntimeException.class, () -> parse("\u0000"));
    Assert.assertThrows(RuntimeException.class, () -> parse(null));
  }

  private Object parse(CharSequence sequence) {
    return parser.parse(JsonParseContextFactory.read(sequence));
  }

}
