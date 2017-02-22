package com.gvertx.core;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gvertx.core.models.Result;
import io.vertx.rxjava.core.http.HttpServerResponse;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

/**
 * Created by wangziqing on 17/2/20.
 */
public abstract class ResultWrite {
    static private final JsonFactory jsonFactory = new JsonFactory();
    static private final ObjectMapper mapper = new ObjectMapper();

    public void writeResult(HttpServerResponse response, Result result) {
        for (Map.Entry<String, String> header : result.getHeaders().entrySet()) {
            response.putHeader(header.getKey(), header.getValue());
        }
        response.putHeader("content-type", String.format("%s; %s", result.getContentType(), result.getCharset()));
        try (StringWriter sw = new StringWriter();
             JsonGenerator gen = jsonFactory.createGenerator(sw)) {
            mapper.writeValue(gen, result.getRenderable());
            response.end(sw.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
