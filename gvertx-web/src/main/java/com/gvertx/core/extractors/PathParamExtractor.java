package com.gvertx.core.extractors;

import com.google.inject.Inject;
import com.gvertx.core.models.Context;
import com.gvertx.core.params.ArgumentClassHolder;
import com.gvertx.core.params.ArgumentExtractor;
import com.gvertx.core.params.ArgumentExtractorChain;
import com.gvertx.core.params.Param;

/**
 * Created by wangziqing on 17/2/22.
 */
public class PathParamExtractor implements ArgumentExtractor<Object> {

    private final ArgumentClassHolder argumentClassHolder;

    private final Param param;

    @Inject
    public PathParamExtractor(Param param,ArgumentClassHolder argumentClassHolder) {
        this.param = param;
        this.argumentClassHolder = argumentClassHolder;
    }


    @Override
    public void extract(ArgumentExtractorChain<Object> argumentExtractorChain, Context context) {
        argumentExtractorChain.next(context,context.getRoutingContext().pathParam(param.value()));
    }

}
