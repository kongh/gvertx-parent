package com.gvertx.core.params;

import com.gvertx.core.ResultWrite;
import com.gvertx.core.models.Context;
import com.gvertx.core.models.EndModel;
import com.gvertx.core.models.Result;

/**
 * Created by wangziqing on 17/2/20.
 */
public class ArgumentExtractorChainImpl<T> extends ResultWrite implements ArgumentExtractorChain<T> {
    private final ArgumentExtractor argumentExtractor;
    private final ArgumentExtractorChain next;
    private final int index;
    private final EndModel endModel;


    public ArgumentExtractorChainImpl(ArgumentExtractor argumentExtractor, EndModel endModel, int index, ArgumentExtractorChain next) {
        this.argumentExtractor = argumentExtractor;
        this.next = next;
        this.index = index;
        this.endModel = endModel;
    }

    @Override
    public void next(Context context, T t) {
        if (null != t) {
            endModel.getParameterObjs()[index] = t;
        }
        argumentExtractor.extract(next, context);
    }

    @Override
    public void end(Context context, Result result) {
        writeResult(context.getRoutingContext().response(), result);
    }
}
