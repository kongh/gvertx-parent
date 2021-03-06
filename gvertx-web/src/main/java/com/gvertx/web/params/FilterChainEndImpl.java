package com.gvertx.web.params;

import com.gvertx.web.utils.WriteHelp;
import com.gvertx.web.models.Context;
import com.gvertx.web.models.Result;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.ext.web.templ.ThymeleafTemplateEngine;


/**
 * Created by wangziqing on 17/2/20.
 */
public class FilterChainEndImpl implements FilterChain {

    private final ArgumentExtractorChain extractorChain;

    private final Vertx vertx;

    private final ThymeleafTemplateEngine engine;

    public FilterChainEndImpl(ArgumentExtractorChain extractorChain,Vertx vertx, ThymeleafTemplateEngine engine) {
        this.extractorChain = extractorChain;
        this.vertx = vertx;
        this.engine = engine;
    }

    @Override
    public void next(Context context) {
        extractorChain.next(context,null);
    }

    @Override
    public void end(Context context, Result result) {
        WriteHelp.end(context.getRoutingContext(),result,vertx, engine);
    }

}
