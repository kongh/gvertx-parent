package com.gvertx.core;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.gvertx.core.constant.HttpConstant;
import com.gvertx.core.models.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangziqing on 17/2/16.
 */
@Singleton
public class GrouterImpl implements Grouter {

    private final List<RouteBuilderImpl> allRouteBuilders = new ArrayList<>();

    private boolean isCompiled;

    private Injector injector;

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
    public RouteBuilderImpl PATCH() {
        RouteBuilderImpl gRouterBuild = new RouteBuilderImpl(HttpConstant.Method.PATCH);
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
    public RouteBuilderImpl HEAD() {
        RouteBuilderImpl gRouterBuild = new RouteBuilderImpl(HttpConstant.Method.HEAD);
        allRouteBuilders.add(gRouterBuild);
        return gRouterBuild;
    }

    @Override
    public RouteBuilderImpl OPTIONS() {
        RouteBuilderImpl gRouterBuild = new RouteBuilderImpl(HttpConstant.Method.OPTIONS);
        allRouteBuilders.add(gRouterBuild);
        return gRouterBuild;
    }

    @Override
    public void compileRoutes(io.vertx.rxjava.ext.web.Router router) {
        if (isCompiled) {
            throw new IllegalStateException("Routes already compiled");
        }
        isCompiled = true;
        long s = System.currentTimeMillis();
        for (RouteBuilderImpl routeBuilder : allRouteBuilders) {
            if(null == routeBuilder){
                continue;
            }
            routeBuilder.buildRoute(injector);
            io.vertx.rxjava.ext.web.Route ret = null;
            switch (routeBuilder.getHttpMethod()) {
                case GET:
                    ret = router.get(routeBuilder.getPath());
                    break;
                case POST:
                    ret = router.post(routeBuilder.getPath());
                    break;
                case PUT:
                    ret = router.put(routeBuilder.getPath());
                    break;
                case DELETE:
                    ret = router.delete(routeBuilder.getPath());
                    break;
                case PATCH:
                    ret = router.patch(routeBuilder.getPath());
                    break;
                case HEAD:
                    ret = router.head(routeBuilder.getPath());
                    break;
                case OPTIONS:
                    ret = router.options(routeBuilder.getPath());
                    break;
            }
            ret.handler(routingContext -> routeBuilder.getFilterChain().
                    next(new Context(routingContext,routeBuilder.getParameters())));
        }
        System.out.println(String.format("编译router 耗时:%s",System.currentTimeMillis() - s));
    }
}
