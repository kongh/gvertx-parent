package com.gvertx.core.params;

import com.gvertx.core.utils.WriteHelp;
import com.gvertx.core.models.Context;
import com.gvertx.core.models.Result;
import io.vertx.rxjava.core.Vertx;


/**
 * Created by wangziqing on 17/2/20.
 */
public class FilterChainEndImpl implements FilterChain {

    private final ArgumentExtractorChain extractorChain;

    private final Vertx vertx;

    public FilterChainEndImpl(ArgumentExtractorChain extractorChain,Vertx vertx) {
        this.extractorChain = extractorChain;
        this.vertx = vertx;
    }

    @Override
    public void next(Context context) {
        extractorChain.next(context,null);
    }

    @Override
    public void end(Context context, Result result) {
        WriteHelp.end(context.getRoutingContext(),result,vertx);
    }

}
