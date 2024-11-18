package io.github.aparx.jsonic.core.parser.syntax;

import io.github.aparx.jsonic.core.JsonSymbol;
import io.github.aparx.jsonic.core.parser.context.JsonParseContext;
import io.github.aparx.jsonic.core.parser.error.JsonParseError;
import io.github.aparx.jsonic.core.parser.error.ParseErrorFactory;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.checkerframework.org.apache.commons.text.CharacterPredicate;

import java.util.NoSuchElementException;

/**
 * @author aparx (Vinzent Z.)
 * @version 2024-11-15 14:25
 * @since 1.0
 */
@DefaultQualifier(NonNull.class)
public interface JsonSyntaxReader {

  ParseErrorFactory errorFactory();

  /**
   * Reads the next character and the next consecutive characters until {@code skipPredicate}
   * returns false. It is guaranteed, that {@code context} is at least skipped by one character,
   * since this method guarantees at least one next read.
   * <p>Example, using whitespace:
   * <pre><code>
   *   var context = JsonParseContextFactory.read("a  b");
   *   readAndSkip(context, Character::isWhitespace);
   *   context.current(); ::= "b"
   * </code></pre>
   * <p>Example, using no whitespace:
   * <pre><code>
   *   var context = JsonParseContextFactory.read("a");
   *   readAndSkip(context, Character::isWhitespace);
   * > Throws error: JsonParseContextFactory#read reads the first character, and #readAndSkip reads
   * > at least one next character, but there is no next character.
   * </code></pre>
   *
   * @param context       the context, supplying the characters
   * @param skipPredicate character predicate that causes this method to continuously read the next
   *                      character in {@code context}, until it returns false
   * @throws NoSuchElementException if {@code context} has no characters left
   */
  void readAndSkip(JsonParseContext context, CharacterPredicate skipPredicate);

  /**
   * Expects {@code symbol} at current character in {@code context} and throws an exception, if the
   * current character does not match the symbol's character.
   * <p>The given symbol's literal is compared against the <i>current</i> character from context.
   *
   * @param context the context supplying the characters to expect the symbol
   * @param symbol  the symbol to be expected, whose literal value defines the expected literal(s)
   * @throws JsonParseError         if the current character in {@code context} does
   *                                not match the literal version of {@code symbol}
   * @throws NoSuchElementException if {@code context} has no characters left
   */
  void expectSymbol(JsonParseContext context, JsonSymbol symbol);

  /**
   * Expects {@code literal} inclusively beginning at current character in {@code context} and
   * throws an exception, if the current and next context characters do not match the literal.
   * <p>Beginning with the current character from given context, each character is compared against
   * the next character in {@code literal}, in consecutive order. If a character in {@code context}
   * is not matching the expected character in {@code literal} (at equal index), an exception is
   * thrown.
   *
   * @param context the context supplying the characters to expect the literal string
   * @param literal the literal to be expected
   * @throws JsonParseError         if a character from {@code context}, does not match an expected
   *                                character from {@code literal} (see specification)
   * @throws NoSuchElementException if {@code context} has no characters left
   * @apiNote More specifically, an error is thrown, if a character in {@code context} at index
   * {@code i}, where {@code i=0} is the current character, does not match a character in {@code
   * literal} at index {@code i} (or thus lengths mismatch).
   */
  void expectLiteral(JsonParseContext context, String literal);

  /**
   * Expects {@code literal} at current character in {@code context} and throws an exception,
   * if the current character does not match the literal.
   *
   * @param context the context whose current character is used to compare against literal
   * @param literal the literal to be expected
   * @throws JsonParseError         if the current character in {@code context} is not equal
   *                                the given {@code literal} character
   * @throws NoSuchElementException if {@code context} has no characters left
   */
  void expectLiteral(JsonParseContext context, char literal);

}