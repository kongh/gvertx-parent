package com.gvertx.core.extractors;

import com.gvertx.core.models.Context;
import com.gvertx.core.params.ArgumentExtractor;
import com.gvertx.core.params.ArgumentExtractorChain;
import com.gvertx.core.params.ParamParsers;

/**
 * Created by wangziqing on 17/2/22.
 */
public class ParamExtractor implements ArgumentExtractor<Object> {

    private String paramName;

    private Class<?> parameterClass;

    public ParamExtractor(String paramName,Class<?> parameterClass) {
        this.paramName = paramName;
        this.parameterClass = parameterClass;
    }

    @Override
    public void extract(ArgumentExtractorChain<Object> argumentExtractorChain, Context context) {
        String val = context.getRoutingContext().request().getParam(paramName);
        argumentExtractorChain.next(context,ParamParsers.parse(parameterClass,val));
    }
}
