package com.gvertx.core;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.gvertx.core.constant.HttpConstant;
import com.gvertx.core.models.Result;
import com.gvertx.core.models.RouterRegister;
import com.gvertx.core.params.ArgumentExtractor;
import com.gvertx.core.params.Param;
import com.gvertx.core.params.PathParam;
import com.gvertx.core.params.WithArgumentExtractor;
import io.vertx.rxjava.core.http.HttpServerResponse;
import io.vertx.rxjava.ext.web.Route;
import io.vertx.rxjava.ext.web.Router;
import io.vertx.rxjava.ext.web.RoutingContext;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by wangziqing on 17/2/16.
 */
public class GrouterImpl implements Grouter {

    private final List<RouteBuilderImpl> allRouteBuilders = new ArrayList<>();

    private boolean isCompiled;

    private Injector injector;

    static private final JsonFactory jsonFactory = new JsonFactory();
    static private final ObjectMapper mapper = new ObjectMapper();

    @Inject
    public GrouterImpl(Injector injector) {
        this.injector = injector;
    }

    @Override
    public RouteBuilderImpl GET() {
        RouteBuilderImpl gRouterBuild = new RouteBuilderImpl(HttpConstant.Method.GET);
        allRouteBuilders.add(gRouterBuild);
        return gRouterBuild;
    }

    @Override
    public RouteBuilderImpl POST() {
        RouteBuilderImpl gRouterBuild = new RouteBuilderImpl(HttpConstant.Method.POST);
        allRouteBuilders.add(gRouterBuild);
        return gRouterBuild;
    }

    @Override
    public RouteBuilderImpl PUT() {
        RouteBuilderImpl gRouterBuild = new RouteBuilderImpl(HttpConstant.Method.PUT);
        allRouteBuilders.add(gRouterBuild);
        return gRouterBuild;
    }

    @Override
    public RouteBuilderImpl DELETE() {
        RouteBuilderImpl gRouterBuild = new RouteBuilderImpl(HttpConstant.Method.DELETE);
        allRouteBuilders.add(gRouterBuild);
        return gRouterBuild;
    }

    @Override
    public void compileRoutes(Router router) {
        if (isCompiled) {
            throw new IllegalStateException("Routes already compiled");
        }
        isCompiled = true;

        long s = System.currentTimeMillis();
        for (RouteBuilderImpl routeBuilder : allRouteBuilders) {
            RouterRegister rrg = routeBuilder.build();
            Route ret = null;
            switch (rrg.getHttpMethod()) {
                case GET:
                    ret = router.get(rrg.getPath());
                    break;
                case POST:
                    ret = router.post(rrg.getPath());
                    break;
                case PUT:
                    ret = router.put(rrg.getPath());
                    break;
                case DELETE:
                    ret = router.delete(rrg.getPath());
                    break;
            }

            Method method = rrg.getInvokeMethod();
            Annotation[][] parameterAnnotations = method.getParameterAnnotations();
            Class[] parameterTypes = method.getParameterTypes();

            byte parameterSize = new Integer(parameterTypes.length).byteValue();
            Object[] parameterObjs = new Object[parameterSize];
            ArgumentExtractor[] argumentExtractors = new ArgumentExtractor[parameterSize];
            String[] paramNames = new String[parameterSize];
            String[] pathParamNames = new String[parameterSize];

            for (byte i = 0; i < parameterAnnotations.length; i++) {
                for (Annotation annotation : parameterAnnotations[i]) {
                    if (annotation instanceof Param) {
                        paramNames[i] = ((Param) annotation).value();
                    } else if (annotation instanceof PathParam) {
                        pathParamNames[i] = ((PathParam) annotation).value();
                    } else {
                        WithArgumentExtractor withArgumentExtractor = annotation.annotationType()
                                .getAnnotation(WithArgumentExtractor.class);

                        if (null != withArgumentExtractor) {
                            ArgumentExtractor argumentExtractor = injector.createChildInjector(new AbstractModule() {
                                @Override
                                protected void configure() {
                                    bind((Class<Annotation>) annotation.annotationType()).toInstance(annotation);
                                }
                            }).getInstance(withArgumentExtractor.value());
                            argumentExtractors[i] = argumentExtractor;
                        }
                    }
                }
            }


            Object object = injector.getInstance(rrg.getCls());
            ret.handler(routingContext -> {

                for (int b = 0; b < parameterSize; b++) {
                    ArgumentExtractor argumentExtractor;
                    if (null != (argumentExtractor = argumentExtractors[b])) {
                        Object val = argumentExtractor.extract(routingContext);
                        parameterObjs[b] = val;
                    } else {
                        String temp;
                        if (!Strings.isNullOrEmpty(temp = paramNames[b])) {
                            parameterObjs[b] = routingContext.request().getParam(temp);
                        } else if (!Strings.isNullOrEmpty(temp = pathParamNames[b])) {
                            parameterObjs[b] = routingContext.pathParam(temp);
                        }
                    }
                }
                Object result = rrg.getMethodAccess().invoke(object, method.getName(), parameterObjs);
                HttpServerResponse response = routingContext.response();

                if (null == result) {
                    response.putHeader("content-type", "application/json; charset=utf-8");
                    response.end("null");
                    return;
                }
                if (result instanceof Result) {
                    Result r = (Result) result;
                    for (Map.Entry<String, String> header : r.getHeaders().entrySet()) {
                        response.putHeader(header.getKey(), header.getValue());
                    }
                    response.putHeader("content-type", String.format("%s; %s", r.getContentType(), r.getCharset()));
                    try (StringWriter sw = new StringWriter();
                         JsonGenerator gen = jsonFactory.createGenerator(sw)) {
                        mapper.writeValue(gen, r.getRenderable());
                        response.end(sw.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    response.putHeader("content-type", "application/json; charset=utf-8");
                    response.end("viod");
                }

            });
        }
        System.out.println("router处理耗时:" + (System.currentTimeMillis() - s));

    }

    private void routerHandler(RoutingContext routingContext) {

    }


}
