package com.gvertx.web;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.gvertx.guice.GuiceVerticleFactory;
import com.gvertx.guice.GuiceVerticleLoader;
import com.gvertx.run.Runner;
import io.vertx.core.*;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.ext.web.Router;
import io.vertx.rxjava.ext.web.handler.BodyHandler;
import io.vertx.rxjava.ext.web.handler.CookieHandler;

import java.util.List;

/**
 * Created by wangziqing on 17/3/12.
 */
public abstract class RestAbstractVerticle implements Verticle {
    protected io.vertx.rxjava.core.Vertx vertx;
    protected Context context;
    private Vertx v;

    @Inject
    private Provider<RouterHelp> routerHelpProvider;


    private RouterHelp routerHelp;
    protected Router router;

    public RestAbstractVerticle(){
    }

    public final void init(Vertx vertx, Context context) {
        this.v = vertx;
        this.vertx = new io.vertx.rxjava.core.Vertx(getVertx());
        this.context = context;
    }

    protected Router configVertxRouter(){
        Router router = Router.router(vertx);
        router.route().handler(CookieHandler.create());
        router.route().handler(BodyHandler.create());
        return router;
    }

    public abstract void start() throws Exception;


    public void stop() throws Exception {
    }


    public final RouteBuilderImpl GET(){
        return routerHelp.GET();
    }
    public final RouteBuilderImpl POST(){
        return routerHelp.POST();
    }
    public final RouteBuilderImpl PUT(){
        return routerHelp.PUT();
    }
    public final RouteBuilderImpl PATCH(){
        return routerHelp.PATCH();
    }
    public final RouteBuilderImpl DELETE(){
        return routerHelp.DELETE();
    }
    public final RouteBuilderImpl HEAD(){
        return routerHelp.HEAD();
    }
    public final RouteBuilderImpl OPTIONS(){
        return routerHelp.OPTIONS();
    }

    public final RouteBuilderImpl get(){
        return routerHelp.GET();
    }
    public final RouteBuilderImpl post(){
        return routerHelp.POST();
    }
    public final RouteBuilderImpl put(){
        return routerHelp.PUT();
    }
    public final RouteBuilderImpl patch(){
        return routerHelp.PATCH();
    }
    public final RouteBuilderImpl delete(){
        return routerHelp.DELETE();
    }
    public final RouteBuilderImpl head(){
        return routerHelp.HEAD();
    }
    public final RouteBuilderImpl options(){
        return routerHelp.OPTIONS();
    }





    public final void start(Future<Void> startFuture) throws Exception {
        this.router = configVertxRouter();
        this.routerHelp = routerHelpProvider.get();
        this.start();
        routerHelp.compileRoutes(router);
        startFuture.complete();
    }




    @Override
    public final Vertx getVertx() {
        return v;
    }

    public final String deploymentID() {
        return this.context.deploymentID();
    }

    public final JsonObject config() {
        return this.context.config();
    }

    public final List<String> processArgs() {
        return this.context.processArgs();
    }


    public final void stop(Future<Void> stopFuture) throws Exception {
        this.stop();
        stopFuture.complete();
    }

    public final void install(Class<? extends RestAbstractVerticle> webAbstractVerticle){
        DeploymentOptions deploymentOptions =  new DeploymentOptions().setConfig(new JsonObject()
                .put(GuiceVerticleLoader.MODULES, new JsonArray().add(Runner.webModule)));
        v.deployVerticle(GuiceVerticleFactory.PREFIX + ":" + webAbstractVerticle.getName(),deploymentOptions);
    }


}
