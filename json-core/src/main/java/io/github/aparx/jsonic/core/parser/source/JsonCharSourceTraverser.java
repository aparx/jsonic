package io.github.aparx.jsonic.core.parser.source;

import io.github.aparx.jsonic.core.context.JsonProcessContext;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.com.google.errorprone.annotations.CanIgnoreReturnValue;
import org.checkerframework.common.value.qual.IntRange;
import org.checkerframework.framework.qual.DefaultQualifier;

/**
 * @author aparx (Vinzent Z.)
 * @version 2024-11-18 23:41
 * @since 1.0
 */
@DefaultQualifier(NonNull.class)
public interface JsonCharSourceTraverser extends JsonCharSource {

  int NULL_CHARACTER = -1;

  boolean hasRead();

  @CanIgnoreReturnValue
  @IntRange(from = NULL_CHARACTER, to = Character.MAX_VALUE)
  int peek();

  char current();

  JsonProcessContext context();

}
