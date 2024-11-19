package io.github.aparx.jsonic.core.parser.error;

import io.github.aparx.jsonic.core.error.JsonicProcessError;

/**
 * @author aparx (Vinzent Z.)
 * @version 2024-11-18 01:25
 * @since 1.0
 */
public class JsonParseError extends JsonicProcessError {

  public JsonParseError() {}

  public JsonParseError(String message) {
    super(message);
  }

  public JsonParseError(String message, Throwable cause) {
    super(message, cause);
  }

  public JsonParseError(Throwable cause) {
    super(cause);
  }

  public JsonParseError(String message, Throwable cause, boolean enableSuppression,
                        boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
