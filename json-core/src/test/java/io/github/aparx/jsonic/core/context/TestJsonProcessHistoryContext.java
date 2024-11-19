package io.github.aparx.jsonic.core.context;

import org.checkerframework.org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author aparx (Vinzent Z.)
 * @version 2024-11-19 00:44
 * @since 1.0
 */
public class TestJsonProcessHistoryContext {

  @Test
  public void testGetHistory_EnsureRightHistory() {
    JsonProcessHistoryContext historyContext = new JsonProcessHistoryContext(3);
    Assert.assertEquals(StringUtils.EMPTY, historyContext.getHistory());
    append(historyContext, "hello");
    Assert.assertEquals("llo", historyContext.getHistory());
    Assert.assertEquals("llo", historyContext.getHistory());
    append(historyContext, "world");
    Assert.assertEquals("rld", historyContext.getHistory());
    Assert.assertEquals("rld", historyContext.getHistory());
    append(historyContext, "!");
    Assert.assertEquals("ld!", historyContext.getHistory());

    historyContext = new JsonProcessHistoryContext(2);
    append(historyContext, "Hello world");
  }

  @Test
  public void testGetHistoryWithMaxLength_EnsureRightHistory() {
    JsonProcessHistoryContext historyContext = new JsonProcessHistoryContext(5);
    append(historyContext, "hello you!");
    Assert.assertEquals(" you!", historyContext.getHistory());
    Assert.assertEquals(StringUtils.EMPTY, historyContext.getHistory(0));
    Assert.assertEquals("!", historyContext.getHistory(1));
    Assert.assertEquals("ou!", historyContext.getHistory(3));
    Assert.assertEquals("you!", historyContext.getHistory(4));
    Assert.assertEquals(" you!", historyContext.getHistory(5));
    append(historyContext, "11");
    Assert.assertEquals("ou!11", historyContext.getHistory(5));
  }


  @Test
  public void testGetHistoryWithMaxLength_ThrowsError() {
    JsonProcessHistoryContext historyContext = new JsonProcessHistoryContext(3);
    Assert.assertThrows(RuntimeException.class, () -> new JsonProcessHistoryContext(0));
    Assert.assertThrows(RuntimeException.class, () -> new JsonProcessHistoryContext(-1));
    Assert.assertThrows(RuntimeException.class, () -> historyContext.getHistory(-1));
    Assert.assertThrows(RuntimeException.class, () -> historyContext.getHistory(4));
  }

  @Test
  public void testGetHistory_EnsureNewlineResetsHistory() {
    JsonProcessHistoryContext historyContext = new JsonProcessHistoryContext(3);
    append(historyContext, "hello");
    Assert.assertEquals("llo", historyContext.getHistory());
    historyContext.newline();
    Assert.assertEquals(StringUtils.EMPTY, historyContext.getHistory());
  }

  @Test
  public void testGetLineIndex_EnsureCorrectIndex() {
    JsonProcessHistoryContext historyContext = new JsonProcessHistoryContext(10);
    append(historyContext, "Hello world");
    Assert.assertEquals(0, historyContext.getLineIndex());
    historyContext.newline();
    Assert.assertEquals(1, historyContext.getLineIndex());
    historyContext.newline();
    Assert.assertEquals(2, historyContext.getLineIndex());
    append(historyContext, "How are you?");
    Assert.assertEquals(2, historyContext.getLineIndex());
    historyContext.newline();
    Assert.assertEquals(3, historyContext.getLineIndex());
    historyContext.append('\n');
    Assert.assertEquals(3, historyContext.getLineIndex());
  }

  @Test
  public void testGetLineIndex_EnsureNewlineAppendIsIgnored() {
    JsonProcessHistoryContext historyContext = new JsonProcessHistoryContext(10);
    historyContext.newline();
    Assert.assertEquals(1, historyContext.getLineIndex());
    historyContext.append('\n');
    Assert.assertEquals(1, historyContext.getLineIndex());
    append(historyContext, "\r\n");
    Assert.assertEquals(1, historyContext.getLineIndex());
    historyContext.newline();
    Assert.assertEquals(2, historyContext.getLineIndex());
    append(historyContext, "\r\n");
    Assert.assertEquals(2, historyContext.getLineIndex());
    historyContext.append('\n');
    Assert.assertEquals(2, historyContext.getLineIndex());
  }

  @Test
  public void testGetCharacterPos_EnsureCorrectIndex() {
    JsonProcessHistoryContext historyContext = new JsonProcessHistoryContext(10);
    Assert.assertEquals(0, historyContext.getPositionInLine());
    append(historyContext, "Foo Bar!");
    Assert.assertEquals(8, historyContext.getPositionInLine());
    historyContext.append('\n');
    Assert.assertEquals(9, historyContext.getPositionInLine());
    append(historyContext, "\r\n");
    Assert.assertEquals(11, historyContext.getPositionInLine());
    append(historyContext, "you!");
    Assert.assertEquals(15, historyContext.getPositionInLine());
  }

  @Test
  public void testGetCharacterPos_EnsureEnterNewlineResets() {
    JsonProcessHistoryContext historyContext = new JsonProcessHistoryContext(10);
    Assert.assertEquals(0, historyContext.getPositionInLine());
    append(historyContext, "Tell me");
    Assert.assertEquals(7, historyContext.getPositionInLine());
    historyContext.append('!');
    Assert.assertEquals(8, historyContext.getPositionInLine());
    historyContext.append('\n');
    Assert.assertEquals(9, historyContext.getPositionInLine());
    historyContext.newline();
    Assert.assertEquals(0, historyContext.getPositionInLine());
    append(historyContext, "Foo");
    Assert.assertEquals(3, historyContext.getPositionInLine());
    historyContext.newline();
    Assert.assertEquals(0, historyContext.getPositionInLine());
  }

  public void append(JsonProcessHistoryContext history, String string) {
    if (string.isEmpty()) return;
    int length = string.length();
    for (int i = 0; i < length; ++i)
      history.append(string.charAt(i));
  }
}
