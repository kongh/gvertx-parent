package com.gvertx.web.params;

import com.gvertx.web.models.Context;

/**
 * Created by wangziqing on 17/2/18.
 */
public interface ArgumentExtractor<T>{
    void extract(ArgumentExtractorChain<T> argumentExtractorChain,Context context);
}
