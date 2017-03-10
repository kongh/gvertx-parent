package com.acme.rxjava.service.test;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import rpc.RequestProp;

import java.util.concurrent.TimeUnit;


/**
 * Created by wangziqing on 17/3/3.
 */

public interface TestService {

     @RequestProp(timeout = 1, timeUnit = TimeUnit.DAYS, retry = 5)
     void save(JsonObject jsonObject,Handler<AsyncResult<JsonObject>> resultHandler);

}
