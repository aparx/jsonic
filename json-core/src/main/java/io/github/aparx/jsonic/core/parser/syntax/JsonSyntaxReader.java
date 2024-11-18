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
   * <p>This method is similar to {@link #read(JsonParseContext, SyntaxReadPredicate)}, with the
   * difference being, that this method is ensuring at least one read and the context current
   * character is the next character where {@code skipPredicate} returned false.
   *
   * @param context       the context, supplying the characters
   * @param skipPredicate character predicate that causes this method to continuously read the next
   *                      character in {@code context}, until it returns false
   * @throws NoSuchElementException if {@code context} has no characters left
   * @see #read(JsonParseContext, SyntaxReadPredicate)
   */
  void nextAndSkip(JsonParseContext context, CharacterPredicate skipPredicate);

  /**
   * Method that calls {@code predicate} on each character iterated in {@code context}, inclusively
   * beginning at the current character and using {@code peek} lookahead for every following.
   * If {@code predicate} returns false, the consuming is stopped and the skip remains unnoticed,
   * such that after invocation the context's current character is equal the last character where
   * {@code predicate} still returned true.
   *
   * @param context   the context to consume, so read, the characters from
   * @param predicate predicate accepting the last character (or {@code -1}) and the next character
   *                  as lookahead, returning true if the context should keep on being read.
   * @apiNote More specifically, given a context keeping a sequence of {@code "helLo"} and a
   * predicate that only returns true if it matches {@code 'z'>=nextChar>='a'}, the context
   * current character will remain lowercase {@code 'l'} after invocation, even tho {@code 'L'} was
   * already read. This is possible through a lookahead, using {@link JsonParseContext#peek()}.
   */
  void read(JsonParseContext context, SyntaxReadPredicate predicate);

  /**
   * Accumulates a string for each character in context where {@code predicate} returns true,
   * inclusively beginning at the current character, utilizing {@code peek} lookahead for every
   * consecutive character following. If none return true in {@code predicate}, the returned string
   * is empty.
   *
   * @param context   the context to consume, so read, the characters from
   * @param predicate predicate accepting the last character (or {@code -1}) and the next character
   *                  as lookahead, returning true if the context should keep on being read.
   * @return a string of all characters from {@code context}, where {@code predicate} returned true,
   * in order of appearance, beginning with the {@code context}'s current character.
   * @apiNote More specifically, given a context keeping a sequence of {@code "helLo"} and a
   * predicate that only returns true if it matches {@code 'z'>=nextChar>='a'}, the context
   * current character will remain lowercase {@code 'l'} after invocation, even tho {@code 'L'} was
   * already read. This is possible through a lookahead, using {@link JsonParseContext#peek()}.
   * @implSpec The default implementation uses this
   * {@link #read(JsonParseContext, SyntaxReadPredicate)} method to accumulate the string,
   * indirectly using {@code predicate} within another anonymous predicate, responsible for only
   * pushing the next character into a string buffer, if {@code predicate} returns true. The
   * result of given predicate is used as the result in the anonymous wrapper predicate.
   * @see #read(JsonParseContext, SyntaxReadPredicate)
   */
  default String accumulate(JsonParseContext context, SyntaxReadPredicate predicate) {
    StringBuilder builder = new StringBuilder();
    this.read(context, (lastChar, nextChar) -> {
      if (!predicate.test(lastChar, nextChar))
        return false;
      builder.append(nextChar);
      return true;
    });
    return builder.toString();
  }

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