package io.github.aparx.jsonic.core.parser.error;

import io.github.aparx.jsonic.core.context.JsonProcessContext;
import io.github.aparx.jsonic.core.error.JsonicProcessError;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

/**
 * @author aparx (Vinzent Z.)
 * @version 2024-11-18 01:25
 * @since 1.0
 */
@DefaultQualifier(NonNull.class)
public class JsonParseError extends JsonicProcessError {

  public JsonParseError() {}

  public JsonParseError(@Nullable String message) {
    super(message);
  }

  public JsonParseError(@Nullable String message, JsonProcessContext context) {
    super(message, context);
  }

  public JsonParseError(@Nullable String message, @Nullable Throwable cause) {
    super(message, cause);
  }

  public JsonParseError(@Nullable Throwable cause) {
    super(cause);
  }

  public JsonParseError(@Nullable String message, @Nullable Throwable cause,
                        boolean enableSuppression,
                        boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
