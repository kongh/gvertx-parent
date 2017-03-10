package com.gvertx.core.extractors;

import com.google.inject.Inject;
import com.gvertx.core.models.Context;
import com.gvertx.core.params.*;

/**
 * Created by wangziqing on 17/2/22.
 */
public class ParamExtractor implements ArgumentExtractor<Object> {

    private final ArgumentClassHolder argumentClassHolder;

    private final Param param;

    @Inject
    public ParamExtractor(Param param,ArgumentClassHolder argumentClassHolder) {
        this.param = param;
        this.argumentClassHolder = argumentClassHolder;
    }

    @Override
    public void extract(ArgumentExtractorChain<Object> argumentExtractorChain, Context context) {
        String val = context.getRoutingContext().request().getParam(param.value());
        argumentExtractorChain.next(context, ParamParsers.parse(argumentClassHolder.getArgumentClass(), val));
    }
}
