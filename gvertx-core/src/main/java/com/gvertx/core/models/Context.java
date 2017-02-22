package com.gvertx.core.models;

import io.vertx.rxjava.core.buffer.Buffer;
import io.vertx.rxjava.core.http.HttpServerRequest;
import io.vertx.rxjava.ext.web.Cookie;
import io.vertx.rxjava.ext.web.FileUpload;
import io.vertx.rxjava.ext.web.RoutingContext;
import io.vertx.rxjava.ext.web.Session;

import java.util.Set;

/**
 * Created by wangziqing on 17/2/20.
 */
public class Context {

    private RoutingContext routingContext;
    private HttpServerRequest httpServerRequest;

    public Context(RoutingContext routingContext){
        this.routingContext = routingContext;
        this.httpServerRequest = routingContext.request();
    }

    public Buffer getBody() {
        return routingContext.getBody();
    }

    public Set<FileUpload> fileUploads() {
        return routingContext.fileUploads();
    }

    public Session session() {
        return routingContext.session();
    }

    public int statusCode() {
        return routingContext.statusCode();
    }

    public String getAcceptableContentType() {
        return routingContext.getAcceptableContentType();
    }

    public String pathParam(String name) {
        return routingContext.pathParam(name);
    }

    public String getParam(String name){
        return httpServerRequest.getParam(name);
    }

    public Set<Cookie> cookies() {
        return routingContext.cookies();
    }

    public RoutingContext addCookie(Cookie cookie) {
        return routingContext.addCookie(cookie);
    }

    public Cookie removeCookie(String name) {
        return routingContext.removeCookie(name);
    }

    public Cookie getCookie(String name) {
        return routingContext.getCookie(name);
    }

    public int cookieCount() {
        return routingContext.cookieCount();
    }

    public RoutingContext getRoutingContext(){
        return this.routingContext;
    }
}
