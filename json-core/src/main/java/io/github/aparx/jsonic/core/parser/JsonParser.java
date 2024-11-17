package io.github.aparx.jsonic.core.parser;

import io.github.aparx.jsonic.core.parser.context.JsonParseContext;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.com.google.errorprone.annotations.CheckReturnValue;
import org.checkerframework.framework.qual.DefaultQualifier;

/**
 * @author aparx (Vinzent Z.)
 * @version 2024-11-15 14:21
 * @since 1.0
 */
@FunctionalInterface
@DefaultQualifier(NonNull.class)
public interface JsonParser<T> {

  @CheckReturnValue
  @Nullable T parse(JsonParseContext context);

}
