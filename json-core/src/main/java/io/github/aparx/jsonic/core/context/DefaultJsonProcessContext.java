package io.github.aparx.jsonic.core.context;

/**
 * @author aparx (Vinzent Z.)
 * @version 2024-11-18 23:55
 * @since 1.0
 */
public class DefaultJsonProcessContext implements JsonProcessContext {

  public static DefaultJsonProcessContext DEFAULT = new DefaultJsonProcessContext();

  private int lineIndex = 0;
  private int charPosInLine = 0;

  @Override
  public void enterNewline() {
    ++this.lineIndex;
    this.charPosInLine = 0;
  }

  @Override
  public void push(char ch) {
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
