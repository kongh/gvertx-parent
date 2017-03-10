package com.gvertx.core.params;

import com.gvertx.core.Route;
import com.gvertx.core.utils.WriteHelp;
import com.gvertx.core.models.Context;
import com.gvertx.core.models.Result;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.ext.web.templ.ThymeleafTemplateEngine;

/**
 * Created by wangziqing on 17/2/20.
 */
public class ArgumentExtractorEndChainImpl<T> extends WriteHelp implements ArgumentExtractorChain<T> {
    private final Route route;
    private final int index;
    private final Vertx vertx;
    private final ThymeleafTemplateEngine engine;

    public ArgumentExtractorEndChainImpl(Route route, int index, Vertx vertx, ThymeleafTemplateEngine engine) {
        this.route = route;
        this.index = index;
        this.vertx = vertx;
        this.engine = engine;
    }

    @Override
    public void next(Context context, T t) {
        if (null != t) {
            context.getParameters()[index] = t;
        }
        Result result = (Result) route.invoke(context.getParameters());
        if (result.getContentType().equals(Result.TEXT_HTML) && result.getTemplate() == null){
            result.setTemplate(String.format("views/%s/%s.html",
                    route.getInvokeObj().getClass().getSimpleName(),route.getMethodName()));
        }
        WriteHelp.end(context.getRoutingContext(), result, vertx , engine);
    }

    @Override
    public void end(Context context, Result result) {
        WriteHelp.end(context.getRoutingContext(), (Result) route.invoke(context.getParameters()), vertx, engine);
    }
}
