package io.github.aparx.jsonic.core.parser.context;

import io.github.aparx.jsonic.core.parser.source.JsonCharSource;
import io.github.aparx.jsonic.core.parser.source.JsonCharSourceFactory;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author aparx (Vinzent Z.)
 * @version 2024-11-18 00:00
 * @since 1.0
 */
public class TestJsonCharSourceFactory {

  @Test
  public void testSequenceEnsureCorrectRead() {
    Assert.assertEquals("Hello world", accumulate(JsonCharSourceFactory.from("Hello world")));
    Assert.assertEquals("\t\n", accumulate(JsonCharSourceFactory.from("\t\n")));
    Assert.assertEquals("\r\n", accumulate(JsonCharSourceFactory.from("\r\n")));
    Assert.assertEquals("\n", accumulate(JsonCharSourceFactory.from("\n")));
    Assert.assertEquals("H", accumulate(JsonCharSourceFactory.from("H")));
    Assert.assertEquals(" ", accumulate(JsonCharSourceFactory.from(" ")));
  }

  @Test
  public void testSequenceEnsureErrorsForEmpty() {
    Assert.assertThrows(IllegalArgumentException.class, () -> JsonCharSourceFactory.from(""));
    Assert.assertThrows(Throwable.class, () -> JsonCharSourceFactory.from((CharSequence) null));
  }

  private String accumulate(JsonCharSource source) {
    StringBuilder builder = new StringBuilder();
    while (source.hasNext()) {
      builder.append(source.next());
    }
    return builder.toString();
  }

}

