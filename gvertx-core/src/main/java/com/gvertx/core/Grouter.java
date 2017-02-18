package com.gvertx.core;

import io.vertx.rxjava.ext.web.Router;

/**
 * Created by wangziqing on 17/2/16.
 */
public interface Grouter {
    RouteBuilderImpl GET();
    RouteBuilderImpl POST();
    RouteBuilderImpl PUT();
    RouteBuilderImpl DELETE();

    void compileRoutes(Router router);
}
