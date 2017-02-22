package com.gvertx.core.params;

import com.gvertx.core.models.Context;
import com.gvertx.core.models.Result;

/**
 * Created by wangziqing on 17/2/20.
 */
public interface ArgumentExtractorChain<T> {
    void next(Context context,T t);

    void end(Context context,Result result);
}
