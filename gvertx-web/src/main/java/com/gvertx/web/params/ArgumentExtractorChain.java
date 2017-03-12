package com.gvertx.web.params;

import com.gvertx.web.models.Context;
import com.gvertx.web.models.Result;

/**
 * Created by wangziqing on 17/2/20.
 */
public interface ArgumentExtractorChain<T> {
    void next(Context context,T t);

    void end(Context context,Result result);
}
