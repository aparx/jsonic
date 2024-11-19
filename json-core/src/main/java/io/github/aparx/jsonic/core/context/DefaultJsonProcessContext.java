package io.github.aparx.jsonic.core.context;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

/**
 * @author aparx (Vinzent Z.)
 * @version 2024-11-18 23:55
 * @since 1.0
 */
@DefaultQualifier(NonNull.class)
public class DefaultJsonProcessContext implements JsonProcessContext {

  public static DefaultJsonProcessContext DEFAULT = new DefaultJsonProcessContext();

  protected int lineIndex;
  protected int charPosInLine;

  @Override
  public void newline() {
    ++this.lineIndex;
    this.charPosInLine = 0;
  }

  @Override
  public void append(char ch) {
    ++this.charPosInLine;
  }

  @Override
  public int getLineIndex() {
    return this.lineIndex;
  }

  @Override
  public int getPositionInLine() {
    return this.charPosInLine;
  }
}
