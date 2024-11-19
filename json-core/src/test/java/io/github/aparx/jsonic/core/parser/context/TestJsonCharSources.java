package io.github.aparx.jsonic.core.parser.context;

import io.github.aparx.jsonic.core.parser.source.JsonCharSource;
import io.github.aparx.jsonic.core.parser.source.JsonCharSources;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author aparx (Vinzent Z.)
 * @version 2024-11-18 00:00
 * @since 1.0
 */
public class TestJsonCharSources {

  @Test
  public void testSequenceEnsureCorrectRead() {
    Assert.assertEquals("Hello world", accumulate(JsonCharSources.of("Hello world")));
    Assert.assertEquals("\t\n", accumulate(JsonCharSources.of("\t\n")));
    Assert.assertEquals("\r\n", accumulate(JsonCharSources.of("\r\n")));
    Assert.assertEquals("\n", accumulate(JsonCharSources.of("\n")));
    Assert.assertEquals("H", accumulate(JsonCharSources.of("H")));
    Assert.assertEquals(" ", accumulate(JsonCharSources.of(" ")));
  }

  @Test
  public void testSequenceEnsureErrorsForEmpty() {
    Assert.assertThrows(IllegalArgumentException.class, () -> JsonCharSources.of(""));
    Assert.assertThrows(Throwable.class, () -> JsonCharSources.of((CharSequence) null));
  }

  private String accumulate(JsonCharSource source) {
    StringBuilder builder = new StringBuilder();
    while (source.hasNext()) {
      builder.append(source.next());
    }
    return builder.toString();
  }

}

