package io.github.aparx.jsonic.core.parser.context.tokens;

import io.github.aparx.jsonic.core.parser.context.JsonParseContextFactory;
import io.github.aparx.jsonic.core.parser.JsonParserFactory;
import io.github.aparx.jsonic.core.parser.tokens.JsonArrayParser;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * @author aparx (Vinzent Z.)
 * @version 2024-11-16 11:41
 * @since 1.0
 */
@DefaultQualifier(NonNull.class)
public class TestJsonArrayParser {

  private final JsonArrayParser<String, List<String>> parser = JsonParserFactory.array((ctx) -> {
    // Elements of this parser must match `[a-z]*`
    StringBuilder builder = new StringBuilder();
    for (char ch = ctx.current(); ch >= 'a' && ch <= 'z'; ch = ctx.next()) {
      builder.append(ch);
      if (!ctx.hasNext()) break;
    }
    return builder.toString();
  });

  @Test
  public void testParseSyntaxIsEnforced() {
    Assert.assertThrows(RuntimeException.class, () -> parse("[foo"));
    Assert.assertThrows(RuntimeException.class, () -> parse("foo]"));
    Assert.assertThrows(RuntimeException.class, () -> parse(" [foo]"));
    Assert.assertThrows(RuntimeException.class, () -> parse("[foo bar]"));
    Assert.assertThrows(RuntimeException.class, () -> parse("[foo bar]"));
    Assert.assertThrows(RuntimeException.class, () -> parse("[foo, bar"));
    Assert.assertThrows(RuntimeException.class, () -> parse("foo, bar]"));
    Assert.assertThrows(RuntimeException.class, () -> parse("[A]"));
    Assert.assertEquals(List.of(), parse("[]"));
    Assert.assertEquals(List.of("a"), parse("[a]"));
    Assert.assertEquals(List.of("foo"), parse("[foo]"));
    Assert.assertEquals(List.of("foo", "bar"), parse("[foo, bar]"));
    Assert.assertEquals(List.of("foo", "bar", ""), parse("[foo, bar,]"));
    Assert.assertEquals(List.of("foo", "bar", "baz"), parse("[foo, bar, baz]"));
  }

  private List<String> parse(CharSequence sequence) {
    return parser.parse(JsonParseContextFactory.read(sequence));
  }


}
