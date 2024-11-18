package io.github.aparx.jsonic.core.parser.syntax;

import io.github.aparx.jsonic.core.JsonSymbol;
import io.github.aparx.jsonic.core.parser.context.JsonParseContext;
import io.github.aparx.jsonic.core.parser.error.ParseErrorFactory;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.com.google.common.base.Preconditions;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.checkerframework.org.apache.commons.text.CharacterPredicate;

/**
 * @author aparx (Vinzent Z.)
 * @version 2024-11-15 15:11
 * @since 1.0
 */
@DefaultQualifier(NonNull.class)
public class DefaultJsonSyntaxReader implements JsonSyntaxReader {

  private static final String EXPECTED_SYMBOL_ERROR = "Expected symbol: %s (%s)";
  private static final String EXPECTED_CHARACTER_ERROR = "Expected character: %s";

  private final ParseErrorFactory errorFactory;

  public DefaultJsonSyntaxReader(ParseErrorFactory errorFactory) {
    Preconditions.checkNotNull(errorFactory, "Error factory must not be null");
    this.errorFactory = errorFactory;
  }

  @Override
  public ParseErrorFactory errorFactory() {
    return this.errorFactory;
  }

  @Override
  public void nextAndSkip(JsonParseContext ctx, CharacterPredicate skipPredicate) {
    //noinspection StatementWithEmptyBody
    while (ctx.hasNext() && skipPredicate.test(ctx.next())) ;
  }

  public void read(JsonParseContext context, SyntaxReadPredicate predicate) {
    int lastChar = JsonParseContext.NULL_CHARACTER;
    boolean hasPeekedOnce = false;
    for (int ch = context.current(); ch != JsonParseContext.NULL_CHARACTER; ) {
      if (!predicate.test(lastChar, (char) ch))
        break;
      if (hasPeekedOnce) context.next(); // continue skip
      ch = context.peek();
      hasPeekedOnce = true;
    }
  }

  @Override
  public void expectSymbol(JsonParseContext ctx, JsonSymbol symbol) {
    if (symbol.matches(ctx.current())) return;
    String message = String.format(EXPECTED_SYMBOL_ERROR, symbol.name(), symbol.literal());
    throw this.errorFactory.create(this, ctx, message);
  }

  @Override
  public void expectLiteral(JsonParseContext ctx, String literal) {
    int cursor = 0, len = literal.length();
    for (int ch = ctx.current(); cursor < len; ch = ctx.peek()) {
      if (cursor != 0) ctx.next();
      if (literal.charAt(cursor) != ch) break;
      ++cursor;
      if (!ctx.hasNext()) break;
    }
    if (cursor == len) return;
    // This error occurred, because we have not matched n length, but is required
    String message = String.format(EXPECTED_CHARACTER_ERROR, literal.charAt(cursor));
    throw this.errorFactory.create(this, ctx, message);
  }

  @Override
  public void expectLiteral(JsonParseContext ctx, char literal) {
    if (ctx.current() == literal) return;
    String message = String.format(EXPECTED_CHARACTER_ERROR, literal);
    throw this.errorFactory.create(this, ctx, message);
  }
}