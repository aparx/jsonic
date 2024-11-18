package io.github.aparx.jsonic.core.parser.syntax;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.common.value.qual.IntRange;
import org.checkerframework.framework.qual.DefaultQualifier;

/**
 * @author aparx (Vinzent Z.)
 * @version 2024-11-18 20:28
 * @since 1.0
 */
@FunctionalInterface
@DefaultQualifier(NonNull.class)
public interface SyntaxReadPredicate {

  boolean test(@IntRange(from = -1, to = Character.MAX_VALUE) int lastChar, char nextChar);

}
