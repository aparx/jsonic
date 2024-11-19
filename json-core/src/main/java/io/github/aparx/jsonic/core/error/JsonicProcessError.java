package io.github.aparx.jsonic.core.error;

import io.github.aparx.jsonic.core.context.JsonProcessContext;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

/**
 * @author aparx (Vinzent Z.)
 * @version 2024-11-19 00:23
 * @since 1.0
 */
@DefaultQualifier(NonNull.class)
public class JsonicProcessError extends RuntimeException {

  public JsonicProcessError() {}

  public JsonicProcessError(@Nullable String message) {
    super(message);
  }

  public JsonicProcessError(@Nullable String message, JsonProcessContext context) {
    super(JsonicErrorMessageFactory.create(message, context));
  }

  public JsonicProcessError(@Nullable String message, @Nullable Throwable cause) {
    super(message, cause);
  }

  public JsonicProcessError(@Nullable Throwable cause) {
    super(cause);
  }

  public JsonicProcessError(@Nullable String message, @Nullable Throwable cause,
                            boolean enableSuppression,
                            boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
