package io.github.aparx.jsonic.core;

import io.github.aparx.jsonic.core.parser.ComposableJsonParser;
import io.github.aparx.jsonic.core.parser.JsonParser;
import io.github.aparx.jsonic.core.parser.context.JsonParseContextFactory;
import io.github.aparx.jsonic.core.parser.context.JsonParserFactory;
import io.github.aparx.jsonic.core.parser.context.tokens.JsonStringParser;

import java.util.List;

/**
 * @author aparx (Vinzent Z.)
 * @version 2024-11-15 14:16
 * @since ${VERSION}
 */
public class Main {


  public static void main(String[] args) {
    var recordParser = JsonParserFactory.record(JsonStringParser.DEFAULT, JsonStringParser.DEFAULT);
    System.out.println(recordParser.parse(JsonParseContextFactory.of("{\"hello\":\"world\"}")));

    var arrayParser = JsonParserFactory.boolArray();
    System.out.println(arrayParser.parse(JsonParseContextFactory.of("[true]")));


    JsonParser<?> parser =
        ComposableJsonParser.compose((newParser) -> List.of(
            JsonParserFactory.string(),
            JsonParserFactory.bool(),
            JsonParserFactory.nil(),
            JsonParserFactory.array(newParser),
            JsonParserFactory.record(
                ComposableJsonParser.compose(
                    // Allow keys of records to be strings or null
                    JsonParserFactory.string(),
                    JsonParserFactory.array(JsonParserFactory.string())
                ), newParser
            ).setStrict(true)
        ));
    System.out.println(parser.parse(JsonParseContextFactory.of("{\"hello\":[\"world\"]," +
        "\"roles\":[\"Hello\"],[\"hello\"]:\"true\",[\"hello\"]:\"false\"}")));
  }
}