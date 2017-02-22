package com.gvertx.core.params;

import com.google.common.base.Strings;
import com.gvertx.core.ResultWrite;
import com.gvertx.core.models.Context;
import com.gvertx.core.models.EndModel;
import com.gvertx.core.models.Result;
import io.vertx.rxjava.core.http.HttpServerResponse;

/**
 * Created by wangziqing on 17/2/20.
 */
public class ArgumentExtractorEndChainImpl<T> extends ResultWrite implements ArgumentExtractorChain<T>  {
    private final EndModel endModel;

    private final int index;

    public ArgumentExtractorEndChainImpl(EndModel endModel,int index) {
        this.endModel = endModel;
        this.index = index;
    }

    @Override
    public void next(Context context, T t) {
        if (null != t) {
            endModel.getParameterObjs()[index] = t;
        }
        for (int b = 0; b < endModel.getParameterSize(); b++) {
            String temp;
            if (!Strings.isNullOrEmpty(temp = endModel.getParamNames()[b])) {
                endModel.getParameterObjs()[b] = context.getRoutingContext().request().getParam(temp);
            } else if (!Strings.isNullOrEmpty(temp = endModel.getPathParamNames()[b])) {
                endModel.getParameterObjs()[b] = context.getRoutingContext().pathParam(temp);
            }
        }
        Object objresult = endModel.getMethodAccess().invoke(endModel.getInvokeObj(),
                endModel.getMethodName(),
                endModel.getParameterObjs());

        HttpServerResponse response = context.getRoutingContext().response();
        if (null == objresult) {
            response.putHeader("content-type", "application/json; charset=utf-8");
            response.end("null");
            return;
        }
        if (objresult instanceof Result) {
            writeResult(response, (Result) objresult);
        } else {
            response.putHeader("content-type", "application/json; charset=utf-8");
            response.end("viod");
        }
    }

    @Override
    public void end(Context context, Result result) {
        writeResult(context.getRoutingContext().response(), result);
    }
}
