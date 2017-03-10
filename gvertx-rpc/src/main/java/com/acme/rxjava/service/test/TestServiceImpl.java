package com.acme.rxjava.service.test;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;

/**
 * Created by wangziqing on 17/3/3.
 */
public class TestServiceImpl implements TestService {


    @Override
    public void save(JsonObject jsonObject, Handler<AsyncResult<JsonObject>> resultHandler) {
        System.out.println("save");
        resultHandler.handle(Future.succeededFuture(new JsonObject().put("ww","dd")));
    }


}
