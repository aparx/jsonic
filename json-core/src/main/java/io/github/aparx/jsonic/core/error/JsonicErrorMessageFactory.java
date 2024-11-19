package io.github.aparx.jsonic.core.error;

import io.github.aparx.jsonic.core.context.JsonProcessContext;
import io.github.aparx.jsonic.core.context.JsonProcessHistoryContext;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.com.google.errorprone.annotations.CheckReturnValue;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.checkerframework.org.apache.commons.lang3.StringUtils;

/**
 * @author aparx (Vinzent Z.)
 * @version 2024-11-19 01:32
 * @since 1.0
 */
@DefaultQualifier(NonNull.class)
public final class JsonicErrorMessageFactory {

  private static final String BACKTRACE_ERROR_PREFIX = "|> ";
  private static final int INLINE_HISTORY_LENGTH_CAP = 50;

  private JsonicErrorMessageFactory() {
    throw new AssertionError();
  }

  public static String create(@Nullable String details, JsonProcessContext context) {
    StringBuilder builder = new StringBuilder("Error at line ");
    builder.append(1 + context.getLineIndex()).append(':')
        .append(context.getPositionInLine())
        .append('\n');
    if (context instanceof JsonProcessHistoryContext history)
      insertBacktrace(builder, history);
    if (StringUtils.isNotEmpty(details))
      builder.append(details);
    return builder.toString();
  }

  @CheckReturnValue
  public static String createInlineBacktrace(JsonProcessHistoryContext context) {
    int pos = context.getPositionInLine() - 1;
    if (pos < 0) return StringUtils.EMPTY;
    return context.getHistory(Math.min(pos, INLINE_HISTORY_LENGTH_CAP));
  }

  private static void insertBacktrace(StringBuilder builder, JsonProcessHistoryContext context) {
    String backtrace = createInlineBacktrace(context).trim();
    if (backtrace.isEmpty()) return;
    builder.append(BACKTRACE_ERROR_PREFIX)
        .append(backtrace).append('\n')
        .append(BACKTRACE_ERROR_PREFIX)
        .append(" ".repeat(2 + backtrace.length() - BACKTRACE_ERROR_PREFIX.length()))
        .append("^ ");
  }

}
