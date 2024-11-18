package io.github.aparx.jsonic.core.parser.context.tokens;

import io.github.aparx.jsonic.core.parser.context.JsonParseContextFactory;
import io.github.aparx.jsonic.core.parser.error.JsonParseError;
import io.github.aparx.jsonic.core.parser.tokens.JsonArrayParser;
import io.github.aparx.jsonic.core.parser.tokens.JsonBooleanParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * @author aparx (Vinzent Z.)
 * @version 2024-11-18 01:39
 * @since 1.0
 */
public class TestJsonBooleanParser {

  private final JsonBooleanParser parser = new JsonBooleanParser();

  @Test
  public void testParse_WrongSyntaxThrowsError() {
    Assert.assertThrows(JsonParseError.class, () -> parse("TRUE"));
    Assert.assertThrows(JsonParseError.class, () -> parse("tRue"));
    Assert.assertThrows(JsonParseError.class, () -> parse("tRUe"));
    Assert.assertThrows(JsonParseError.class, () -> parse("trUE"));
    Assert.assertThrows(JsonParseError.class, () -> parse("truE"));
    Assert.assertThrows(JsonParseError.class, () -> parse("True"));
    Assert.assertThrows(JsonParseError.class, () -> parse(" true"));
    Assert.assertThrows(JsonParseError.class, () -> parse("\ttrue"));
    Assert.assertThrows(JsonParseError.class, () -> parse("\r\ntrue"));
    Assert.assertThrows(JsonParseError.class, () -> parse("\u0000true"));

    Assert.assertThrows(JsonParseError.class, () -> parse("FALSE"));
    Assert.assertThrows(JsonParseError.class, () -> parse("fAlse"));
    Assert.assertThrows(JsonParseError.class, () -> parse("fALse"));
    Assert.assertThrows(JsonParseError.class, () -> parse("falSE"));
    Assert.assertThrows(JsonParseError.class, () -> parse("falsE"));
    Assert.assertThrows(JsonParseError.class, () -> parse("False"));
    Assert.assertThrows(JsonParseError.class, () -> parse(" false"));
    Assert.assertThrows(JsonParseError.class, () -> parse("\tfalse"));
    Assert.assertThrows(JsonParseError.class, () -> parse("\r\nfalse"));
    Assert.assertThrows(JsonParseError.class, () -> parse("\u0000false"));
  }

  @Test
  public void testParse_RightSyntaxReturnsRightResults() {
    Assert.assertEquals(true, parse("true"));
    Assert.assertEquals(false, parse("false"));
  }

  @Test
  public void testParse_EnsureEarlyReturn() {
    Assert.assertEquals(false, parse("false\t"));
    Assert.assertEquals(true, parse("true\t"));
    Assert.assertEquals(true, parse("trueSOME_OTHER_THINGS"));
    Assert.assertEquals(false, parse("falseSOME_OTHER_THINGS"));
  }

  @Test
  public void testParse_EmptyThrowsError() {
    Assert.assertThrows(RuntimeException.class, () -> parse(""));
    Assert.assertThrows(RuntimeException.class, () -> parse("\u0000"));
    Assert.assertThrows(RuntimeException.class, () -> parse(null));
  }

  private Boolean parse(CharSequence sequence) {
    return parser.parse(JsonParseContextFactory.read(sequence));
  }

}
