package com.gvertx.core.params;

import io.vertx.rxjava.ext.web.RoutingContext;

/**
 * Created by wangziqing on 17/2/18.
 */
public interface ArgumentExtractor<T> {
    T extract(RoutingContext routingContext);
}
