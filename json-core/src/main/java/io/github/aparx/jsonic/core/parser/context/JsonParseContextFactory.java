package io.github.aparx.jsonic.core.parser.context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @author aparx (Vinzent Z.)
 * @version 2024-11-15 15:23
 * @since 1.0
 */
public final class JsonParseContextFactory {

  private static final JsonSyntaxReader DEFAULT_SYNTAX_READER =
      new DefaultSyntaxReader((__, ctx, msg) -> new IllegalStateException(
          String.format("Error at %s:%s -> %s", 1 + ctx.lineIndex(), ctx.charPosInLine(), msg)));

  private JsonParseContextFactory() {
    throw new AssertionError();
  }

  public static JsonParseContext read(JsonCharSource source, JsonSyntaxReader syntaxReader) {
    JsonParseContext context = new JsonParseContext(source, syntaxReader);
    context.next(); // kickstart initialization by reading first character
    return context;
  }

  public static JsonParseContext read(CharSequence source, JsonSyntaxReader syntaxReader) {
    return read(JsonCharSourceFactory.from(source), syntaxReader);
  }

  public static JsonParseContext read(InputStream source, JsonSyntaxReader syntaxReader) {
    return read(JsonCharSourceFactory.from(source), syntaxReader);
  }

  public static JsonParseContext read(File source, JsonSyntaxReader syntaxReader) throws FileNotFoundException {
    return read(JsonCharSourceFactory.from(source), syntaxReader);
  }

  public static JsonParseContext read(JsonCharSource source) {
    return read(source, DEFAULT_SYNTAX_READER);
  }

  public static JsonParseContext read(CharSequence source) {
    return read(JsonCharSourceFactory.from(source), DEFAULT_SYNTAX_READER);
  }

  public static JsonParseContext read(InputStream source) {
    return read(JsonCharSourceFactory.from(source), DEFAULT_SYNTAX_READER);
  }

  public static JsonParseContext read(File source) throws FileNotFoundException {
    return read(JsonCharSourceFactory.from(source), DEFAULT_SYNTAX_READER);
  }

}
