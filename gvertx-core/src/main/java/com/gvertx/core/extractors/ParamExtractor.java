package com.gvertx.core.extractors;

import com.google.inject.Inject;
import com.gvertx.core.models.Context;
import com.gvertx.core.params.ArgumentExtractor;
import com.gvertx.core.params.ArgumentExtractorChain;
import com.gvertx.core.params.Param;
import io.vertx.core.Vertx;

/**
 * Created by wangziqing on 17/2/22.
 */
public class ParamExtractor implements ArgumentExtractor<Object> {


    private Param param;

    @Inject
    public ParamExtractor(Param param, Vertx vertx) {
        this.param = param;
    }

    @Override
    public void extract(ArgumentExtractorChain<Object> argumentExtractorChain, Context context) {
        System.out.println(param);
        argumentExtractorChain.next(context,"xx");
    }
}
