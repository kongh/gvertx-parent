package com.gvertx.core.params;

import com.gvertx.core.Route;
import com.gvertx.core.utils.WriteHelp;
import com.gvertx.core.models.Context;
import com.gvertx.core.models.Result;
import io.vertx.rxjava.core.Vertx;

/**
 * Created by wangziqing on 17/2/20.
 */
public class ArgumentExtractorEndChainImpl<T> extends WriteHelp implements ArgumentExtractorChain<T> {
    private final Route route;
    private final int index;
    private final Vertx vertx;

    public ArgumentExtractorEndChainImpl(Route route, int index, Vertx vertx) {
        this.route = route;
        this.index = index;
        this.vertx = vertx;
    }

    @Override
    public void next(Context context, T t) {
        if (null != t) {
            context.getParameters()[index] = t;
        }
        WriteHelp.end(context.getRoutingContext(), (Result) route.invoke(context.getParameters()), vertx);
    }

    @Override
    public void end(Context context, Result result) {
        WriteHelp.end(context.getRoutingContext(), (Result) route.invoke(context.getParameters()), vertx);
    }
}
