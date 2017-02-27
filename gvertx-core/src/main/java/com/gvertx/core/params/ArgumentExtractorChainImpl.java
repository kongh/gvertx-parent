package com.gvertx.core.params;

import com.gvertx.core.utils.WriteHelp;
import com.gvertx.core.models.Context;
import com.gvertx.core.models.Result;
import io.vertx.rxjava.core.Vertx;

/**
 * Created by wangziqing on 17/2/20.
 */
public class ArgumentExtractorChainImpl<T> implements ArgumentExtractorChain<T> {
    private final ArgumentExtractor argumentExtractor;
    private final ArgumentExtractorChain next;
    private final int index;
    private final Vertx vertx;

    public ArgumentExtractorChainImpl(ArgumentExtractor argumentExtractor, int index, ArgumentExtractorChain next, Vertx vertx) {
        this.argumentExtractor = argumentExtractor;
        this.next = next;
        this.index = index;
        this.vertx = vertx;
    }

    @Override
    public void next(Context context, T t) {
        if (null != t) {
            context.getParameters()[index] = t;
        }
        argumentExtractor.extract(next, context);
    }

    @Override
    public void end(Context context, Result result) {
        WriteHelp.end(context.getRoutingContext(), result, vertx);
    }
}
