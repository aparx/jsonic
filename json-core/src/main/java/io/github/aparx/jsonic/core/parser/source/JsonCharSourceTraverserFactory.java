package io.github.aparx.jsonic.core.parser.source;

import io.github.aparx.jsonic.core.context.JsonProcessHistoryContext;
import io.github.aparx.jsonic.core.context.JsonProcessContext;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @author aparx (Vinzent Z.)
 * @version 2024-11-15 15:23
 * @since 1.0
 */
public final class JsonCharSourceTraverserFactory {

  private JsonCharSourceTraverserFactory() {
    throw new AssertionError();
  }

  public static JsonCharSourceTraverser read(JsonCharSource source, JsonProcessContext context) {
    JsonCharSourceTraverser traverser = new DefaultJsonCharSourceTraverser(source, context);
    traverser.next(); // kickstart initialization by first read
    return traverser;
  }

  public static JsonCharSourceTraverser read(CharSequence source, JsonProcessContext context) {
    return read(JsonCharSourceFactory.from(source), context);
  }

  public static JsonCharSourceTraverser read(InputStream source, JsonProcessContext context) {
    return read(JsonCharSourceFactory.from(source), context);
  }

  public static JsonCharSourceTraverser read(File source, JsonProcessContext context) throws FileNotFoundException {
    return read(JsonCharSourceFactory.from(source), context);
  }

  public static JsonCharSourceTraverser read(JsonCharSource source) {
    return read(source, new JsonProcessHistoryContext());
  }

  public static JsonCharSourceTraverser read(CharSequence source) {
    return read(JsonCharSourceFactory.from(source), new JsonProcessHistoryContext());
  }

  public static JsonCharSourceTraverser read(InputStream source) {
    return read(JsonCharSourceFactory.from(source), new JsonProcessHistoryContext());
  }

  public static JsonCharSourceTraverser read(File source) throws FileNotFoundException {
    return read(JsonCharSourceFactory.from(source), new JsonProcessHistoryContext());
  }

}
