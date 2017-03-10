package com.gvertx.core;

import com.google.inject.Inject;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.http.HttpServer;
import io.vertx.rxjava.ext.web.Router;
import io.vertx.rxjava.ext.web.handler.BodyHandler;
import io.vertx.rxjava.ext.web.handler.CookieHandler;

/**
 * Created by wangziqing on 17/2/15.
 */
public class Gverticle extends AbstractVerticle {
    static final private Logger log = LoggerFactory.getLogger(Gverticle.class);

    public static final String CONF_ROUTES = "conf.Routes";

    static final int port = 8080;

    @Inject
    private Grouter grouter;

    protected HttpServer httpServer;

    @Override
    public void start() throws Exception {
        Router router = Router.router(vertx);
        router.route().handler(CookieHandler.create());
        router.route().handler(BodyHandler.create());

        if (doesClassExist(CONF_ROUTES)) {
            final Class<? extends ApplicationRoutes> routes =
                    (Class<? extends ApplicationRoutes>) Class.forName(CONF_ROUTES);
            routes.newInstance().init(grouter);
            grouter.compileRoutes(router);
        }
        HttpServerOptions httpServerOptions = new HttpServerOptions();
        httpServerOptions.setCompressionSupported(true);
        httpServer = vertx.createHttpServer(httpServerOptions);

        httpServer.requestHandler(router::accept).rxListen(port).
                subscribe(
                        server -> log.info(String.format("Server is now listening. %s  Thread:%s ", "http://localhost:" + port, Thread.currentThread())),
                        failure -> log.info(String.format("Server could not start. Thread:%s", Thread.currentThread()), failure)
                );

        Runtime runtime = Runtime.getRuntime();
        System.out.println("freeMemory:" + runtime.freeMemory());
        System.out.println("maxMemory:" + runtime.maxMemory());
        System.out.println("totalMemory:" + runtime.totalMemory());
    }


    @Override
    public void stop() throws Exception {
        if (httpServer != null) {
            httpServer.close(res -> {
                if (res.succeeded()) {
                    log.info("Server is closed. Thread:" + Thread.currentThread());
                } else {
                    log.info("Failed to close. Thread:" + Thread.currentThread(), res.cause());
                }
            });
        }
    }



    protected boolean doesClassExist(String nameWithPackage) {
        boolean exists;
        try {
            Class.forName(nameWithPackage, false, this.getClass().getClassLoader());
            exists = true;
        } catch (ClassNotFoundException e) {
            exists = false;
        }
        return exists;
    }
}
