package io.github.aparx.jsonic.core.parser.syntax;

import io.github.aparx.jsonic.core.JsonSymbol;
import io.github.aparx.jsonic.core.parser.error.JsonParseError;
import io.github.aparx.jsonic.core.parser.error.ParseErrorFactory;
import io.github.aparx.jsonic.core.parser.source.JsonCharSourceTraverser;
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
   * returns false. It is guaranteed, that {@code traverser} is at least skipped by one character,
   * since this method guarantees at least one next read.
   * <p>Example, using whitespace:
   * <pre><code>
   *   var traverser = JsonCharSourceTraverser.read("a  b");
   *   readAndSkip(traverser, Character::isWhitespace);
   *   traverser.current(); ::= "b"
   * </code></pre>
   * <p>Example, using no whitespace:
   * <pre><code>
   *   var traverser = JsonCharSourceTraverser.read("a");
   *   readAndSkip(traverser, Character::isWhitespace);
   * > Throws error: JsonCharSourceTraverser#read reads the first character, and #readAndSkip reads
   * > at least one next character, but there is no next character.
   * </code></pre>
   * <p>This method is similar to {@link #read(JsonCharSourceTraverser, SyntaxReadPredicate)},
   * with the
   * difference being, that this method is ensuring at least one read and the traverser current
   * character is the next character where {@code skipPredicate} returned false.
   *
   * @param traverser     the traverser, supplying the characters
   * @param skipPredicate character predicate that causes this method to continuously read the next
   *                      character in {@code traverser}, until it returns false
   * @throws NoSuchElementException if {@code traverser} has no characters left
   * @see #read(JsonCharSourceTraverser, SyntaxReadPredicate)
   */
  void nextAndSkip(JsonCharSourceTraverser traverser, CharacterPredicate skipPredicate);

  /**
   * Method that calls {@code predicate} on each character iterated in {@code traverser},
   * inclusively beginning at the current character and using {@code peek} lookahead for every
   * following. If {@code predicate} returns false, the consuming is stopped and the skip remains
   * unnoticed, such that after invocation the traverser's current character is equal the last
   * character where {@code predicate} still returned true.
   *
   * @param traverser the traverser to consume, so read, the characters from
   * @param predicate predicate accepting the last character (or {@code -1}) and the next character
   *                  as lookahead, returning true if the traverser should keep on being read.
   * @apiNote More specifically, given a traverser keeping a sequence of {@code "helLo"} and a
   * predicate that only returns true if it matches {@code 'z'>=nextChar>='a'}, the traverser
   * current character will remain lowercase {@code 'l'} after invocation, even tho {@code 'L'} was
   * already read. This is possible through a lookahead, using
   * {@link JsonCharSourceTraverser#peek()}.
   */
  void read(JsonCharSourceTraverser traverser, SyntaxReadPredicate predicate);

  /**
   * Accumulates a string for each character in traverser where {@code predicate} returns true,
   * inclusively beginning at the current character, utilizing {@code peek} lookahead for every
   * consecutive character following. If none return true in {@code predicate}, the returned string
   * is empty.
   *
   * @param traverser the traverser to consume, so read, the characters from
   * @param predicate predicate accepting the last character (or {@code -1}) and the next character
   *                  as lookahead, returning true if the traverser should keep on being read.
   * @return a string of all characters from {@code traverser}, where {@code predicate} returned
   * true,
   * in order of appearance, beginning with the {@code traverser}'s current character.
   * @apiNote More specifically, given a traverser keeping a sequence of {@code "helLo"} and a
   * predicate that only returns true if it matches {@code 'z'>=nextChar>='a'}, the traverser
   * current character will remain lowercase {@code 'l'} after invocation, even tho {@code 'L'} was
   * already read. This is possible through a lookahead, using
   * {@link JsonCharSourceTraverser#peek()}.
   * @implSpec The default implementation uses this
   * {@link #read(JsonCharSourceTraverser, SyntaxReadPredicate)} method to accumulate the string,
   * indirectly using {@code predicate} within another anonymous predicate, responsible for only
   * pushing the next character into a string buffer, if {@code predicate} returns true. The
   * result of given predicate is used as the result in the anonymous wrapper predicate.
   * @see #read(JsonCharSourceTraverser, SyntaxReadPredicate)
   */
  default String accumulate(JsonCharSourceTraverser traverser, SyntaxReadPredicate predicate) {
    StringBuilder builder = new StringBuilder();
    this.read(traverser, (lastChar, nextChar) -> {
      if (!predicate.test(lastChar, nextChar))
        return false;
      builder.append(nextChar);
      return true;
    });
    return builder.toString();
  }

  /**
   * Expects {@code symbol} at current character in {@code traverser} and throws an exception, if
   * the
   * current character does not match the symbol's character.
   * <p>The given symbol's literal is compared against the <i>current</i> character from traverser.
   *
   * @param traverser the traverser supplying the characters to expect the symbol
   * @param symbol    the symbol to be expected, whose literal value defines the expected literal(s)
   * @throws JsonParseError         if the current character in {@code traverser} does
   *                                not match the literal version of {@code symbol}
   * @throws NoSuchElementException if {@code traverser} has no characters left
   */
  void expectSymbol(JsonCharSourceTraverser traverser, JsonSymbol symbol);

  /**
   * Expects {@code literal} inclusively beginning at current character in {@code traverser} and
   * throws an exception, if the current and next traverser characters do not match the literal.
   * <p>Beginning with the current character from given traverser, each character is compared
   * against
   * the next character in {@code literal}, in consecutive order. If a character in {@code
   * traverser}
   * is not matching the expected character in {@code literal} (at equal index), an exception is
   * thrown.
   *
   * @param traverser the traverser supplying the characters to expect the literal string
   * @param literal   the literal to be expected
   * @throws JsonParseError         if a character from {@code traverser}, does not match an
   *                                expected
   *                                character from {@code literal} (see specification)
   * @throws NoSuchElementException if {@code traverser} has no characters left
   * @apiNote More specifically, an error is thrown, if a character in {@code traverser} at index
   * {@code i}, where {@code i=0} is the current character, does not match a character in {@code
   * literal} at index {@code i} (or thus lengths mismatch).
   */
  void expectLiteral(JsonCharSourceTraverser traverser, String literal);

  /**
   * Expects {@code literal} at current character in {@code traverser} and throws an exception,
   * if the current character does not match the literal.
   *
   * @param traverser the traverser whose current character is used to compare against literal
   * @param literal   the literal to be expected
   * @throws JsonParseError         if the current character in {@code traverser} is not equal
   *                                the given {@code literal} character
   * @throws NoSuchElementException if {@code traverser} has no characters left
   */
  void expectLiteral(JsonCharSourceTraverser traverser, char literal);

}