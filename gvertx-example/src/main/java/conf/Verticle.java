package conf;


import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.http.HttpServer;
import io.vertx.rxjava.ext.web.Router;
import util.Runner;

/**
 * Created by wangziqing on 17/1/22.
 */
public class Verticle extends AbstractVerticle {

    static final private Logger log = LoggerFactory.getLogger(Verticle.class);

    static final int port = 8080;

    public static void main(String[] args) {
        Runner.runExample(Verticle.class);
    }

    @Override
    public void start() throws Exception {
        Router router = Router.router(vertx);
        router.route().handler(routingContext ->
                routingContext.response().
                        putHeader("content-type", "text/html").
                        end("<html><body><h1>Hello from vert.x!</h1></body></html>")
        );

        HttpServer httpServer = vertx.createHttpServer();
        httpServer.requestHandler(router::accept).
                listenObservable(port).
                subscribe(
                        server -> log.info(String.format("Server is now listening. %s  Thread:%s ", "http://localhost:"+port, Thread.currentThread())),
                        failure -> log.info(String.format("Server could not start. Thread:%s", Thread.currentThread()), failure)
                );
    }
}
