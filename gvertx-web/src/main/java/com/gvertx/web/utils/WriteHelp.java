package com.gvertx.web.utils;

import com.google.common.collect.Maps;
import com.gvertx.web.models.Result;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.core.RxHelper;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.core.http.HttpServerResponse;
import io.vertx.rxjava.ext.web.RoutingContext;
import io.vertx.rxjava.ext.web.templ.ThymeleafTemplateEngine;
import rx.Observable;
import rx.Subscriber;

import java.util.Map;

/**
 * Created by wangziqing on 17/2/20.
 */
public abstract class WriteHelp {

    public final static void end(RoutingContext routingContext, Result result, Vertx vertx, ThymeleafTemplateEngine engine) {
        if(null == result.getObservable()){
            result.setObservable(Observable.empty());
        }
        result.getObservable()
                .subscribeOn(RxHelper.blockingScheduler(vertx)).observeOn(RxHelper.scheduler(vertx))
                .subscribe(new Subscriber<Object>() {
                    private Map<String, Object> map;
                    private Object resultObj;

                    @Override
                    public void onCompleted() {
                        System.out.println(String.format("onCompleted:%s,activeCount:%s", Thread.currentThread().getName(), Thread.activeCount()));
                        endResult(routingContext, result, map == null ? resultObj : map, engine);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        throwable.printStackTrace();
                        routingContext.response().setStatusCode(400).end("error");
                    }

                    @Override
                    public void onNext(Object object) {
                        if (null == this.resultObj) {
                            this.resultObj = object;
                        } else {
                            if (result instanceof Map) {
                                map = (Map) this.resultObj;
                            } else {
                                map = Maps.newLinkedHashMap();
                                map.put(SwissKnife.getRealClassNameLowerCamelCase(this.resultObj), this.resultObj);
                                this.resultObj = map;
                            }

                            String key = SwissKnife.getRealClassNameLowerCamelCase(object);
                            if (map.containsKey(key)) {
                                throw new IllegalArgumentException(
                                        String.format(
                                                "Cannot store object with default name %s."
                                                        + "An object with the same name is already stored."
                                                        + "Consider using render(key, value) to name objects implicitly.",
                                                key));

                            } else {
                                map.put(SwissKnife.getRealClassNameLowerCamelCase(object), object);
                            }
                        }
                    }
                });
    }

    private static void endResult(RoutingContext routingContext, Result result, Object content, ThymeleafTemplateEngine engine) {
        System.out.println(String.format("end-subscribeOn:%s,activeCount:%s", Thread.currentThread().getName(), Thread.activeCount()));
        HttpServerResponse response = routingContext.response();
        for (Map.Entry<String, String> header : result.getHeaders().entrySet()) {
            response.putHeader(header.getKey(), header.getValue());
        }
        if (result.getContentType().equals(Result.TEXT_HTML)) {
            Map<String, Object> map = null;
            if(content instanceof JsonObject){
                JsonObject jsonObject = (JsonObject)content;
                map = jsonObject.getMap();
            }else if (content instanceof Map) {
                map = (Map) content;
            }
            if(null != map){
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    routingContext.put(entry.getKey(), entry.getValue());
                }
            }
            engine.rxRender(routingContext, result.getTemplate()).subscribe(res -> {
                response.end(res);
            }, err -> {
                routingContext.fail(err);
            });
        } else {
            response
                    .setStatusCode(result.getStatusCode())
                    .putHeader("content-type", String.format("%s; %s", result.getContentType(), result.getCharset()))
                    .end(Json.encodePrettily(content));
        }
    }

}
