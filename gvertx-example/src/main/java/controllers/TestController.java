package controllers;

import com.google.inject.Singleton;
import io.vertx.rxjava.core.http.HttpServerResponse;
import io.vertx.rxjava.ext.web.RoutingContext;

/**
 * Created by wangziqing on 17/2/15.
 */
@Singleton
public class TestController {

    public void hello(RoutingContext routingContext){
        HttpServerResponse response = routingContext.response();
        response.putHeader("content-type", "text/plain");
        // Write to the response and end it
        response.end("Hello World from Vert.x-Web!");
    }
}
