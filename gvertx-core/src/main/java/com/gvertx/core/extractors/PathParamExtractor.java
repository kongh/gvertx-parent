package com.gvertx.core.extractors;

import com.gvertx.core.models.Context;
import com.gvertx.core.params.ArgumentExtractor;
import com.gvertx.core.params.ArgumentExtractorChain;

/**
 * Created by wangziqing on 17/2/22.
 */
public class PathParamExtractor implements ArgumentExtractor<Object> {

    private String paramName;

    private Class<?> parameterClass;

    public PathParamExtractor(String paramName, Class<?> parameterClass) {
        this.paramName = paramName;
        this.parameterClass = parameterClass;
    }


    @Override
    public void extract(ArgumentExtractorChain<Object> argumentExtractorChain, Context context) {
        argumentExtractorChain.next(context,context.getRoutingContext().pathParam(paramName));
    }

}
