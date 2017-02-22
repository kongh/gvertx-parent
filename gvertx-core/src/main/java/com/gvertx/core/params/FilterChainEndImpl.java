package com.gvertx.core.params;

import com.gvertx.core.ResultWrite;
import com.gvertx.core.models.Context;
import com.gvertx.core.models.Result;


/**
 * Created by wangziqing on 17/2/20.
 */
public class FilterChainEndImpl extends ResultWrite implements FilterChain {

    private final ArgumentExtractorChain extractorChain;

    public FilterChainEndImpl(ArgumentExtractorChain extractorChain) {
        this.extractorChain = extractorChain;
    }

    @Override
    public void next(Context context) {
        extractorChain.next(context,null);
    }

    @Override
    public void end(Context context, Result result) {
        writeResult(context.getRoutingContext().response(), result);
    }

}
