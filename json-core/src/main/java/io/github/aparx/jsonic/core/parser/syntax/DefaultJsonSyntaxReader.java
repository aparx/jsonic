package io.github.aparx.jsonic.core.parser.syntax;

import io.github.aparx.jsonic.core.JsonSymbol;
import io.github.aparx.jsonic.core.parser.error.JsonParseError;
import io.github.aparx.jsonic.core.parser.error.ParseErrorFactory;
import io.github.aparx.jsonic.core.parser.source.JsonCharSourceTraverser;
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

  public static final JsonSyntaxReader DEFAULT =
      new DefaultJsonSyntaxReader((reader, ctx, msg) -> new JsonParseError(
          String.format("Error at %s:%s -> %s", 1 + ctx.context().getLineIndex(),
              ctx.context().getPositionInLine(), msg)));

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
  public void nextAndSkip(JsonCharSourceTraverser traverser, CharacterPredicate skipPredicate) {
    //noinspection StatementWithEmptyBody
    while (traverser.hasNext() && skipPredicate.test(traverser.next())) ;
  }

  public void read(JsonCharSourceTraverser traverser, SyntaxReadPredicate predicate) {
    int lastChar = JsonCharSourceTraverser.NULL_CHARACTER;
    boolean hasPeekedOnce = false;
    for (int ch = traverser.current(); ch != JsonCharSourceTraverser.NULL_CHARACTER; ) {
      if (!predicate.test(lastChar, (char) ch))
        break;
      if (hasPeekedOnce) traverser.next(); // continue skip
      ch = traverser.peek();
      hasPeekedOnce = true;
    }
  }

  @Override
  public void expectSymbol(JsonCharSourceTraverser traverser, JsonSymbol symbol) {
    if (symbol.matches(traverser.current())) return;
    String message = String.format(EXPECTED_SYMBOL_ERROR, symbol.name(), symbol.literal());
    throw this.errorFactory.create(this, traverser, message);
  }

  @Override
  public void expectLiteral(JsonCharSourceTraverser traverser, String literal) {
    int cursor = 0, len = literal.length();
    for (int ch = traverser.current(); cursor < len; ch = traverser.peek()) {
      if (cursor != 0) traverser.next();
      if (literal.charAt(cursor) != ch) break;
      ++cursor;
      if (!traverser.hasNext()) break;
    }
    if (cursor == len) return;
    // This error occurred, because we have not matched n length, but is required
    String message = String.format(EXPECTED_CHARACTER_ERROR, literal.charAt(cursor));
    throw this.errorFactory.create(this, traverser, message);
  }

  @Override
  public void expectLiteral(JsonCharSourceTraverser traverser, char literal) {
    if (traverser.current() == literal) return;
    String message = String.format(EXPECTED_CHARACTER_ERROR, literal);
    throw this.errorFactory.create(this, traverser, message);
  }
}