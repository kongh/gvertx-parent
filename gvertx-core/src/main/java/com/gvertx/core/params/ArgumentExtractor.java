package com.gvertx.core.params;

import com.gvertx.core.models.Context;

/**
 * Created by wangziqing on 17/2/18.
 */
public interface ArgumentExtractor<T>{
    void extract(ArgumentExtractorChain<T> argumentExtractorChain,Context context);
}
