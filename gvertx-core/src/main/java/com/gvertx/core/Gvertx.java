package com.gvertx.core;


import com.gvertx.core.guice.GuiceVerticleFactory;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

/**
 * Created by wangziqing on 17/1/22.
 */
public class Gvertx {

    static final private Logger log = LoggerFactory.getLogger(Gverticle.class);

    private Consumer<Vertx> runner;

    private DeploymentOptions deploymentOptions;
    private VertxOptions options;

    private Vertx vertx;

    public static void main(String[] args) {

        System.setProperty("cglib.debugLocation", "/Users/wangziqing/github/gvertx-parent/gvertx-metrics/target/classes");

        new Gvertx().run();
    }

    public void run(){
        config().deploy();
    }
    private Gvertx config(){
        options = new VertxOptions();
//        options.setMetricsOptions(new MetricsOptions().setEnabled(true));
//
//        if (ninjaProperties.getBooleanWithDefault(VERTX_IS_METRICS_ENABLED, false)) {
//            options.setMetricsOptions(new MetricsOptions().setEnabled(true));
//        }

        deploymentOptions = new DeploymentOptions()
                .setInstances(1);

//        if (ninjaProperties.getBooleanWithDefault(VERTX_IS_WORKER, true)) {
//            deploymentOptions
//                    .setWorker(true)
//                    .setWorkerPoolName("ninja-vertx");
//        }
        if (options.isClustered()) {
            CountDownLatch countDownLatch = new CountDownLatch(1);
            Vertx.clusteredVertx(options, res -> {
                if (res.succeeded()) {
                    vertx = res.result();
                    countDownLatch.countDown();
                } else {
                    log.error(res.cause().getMessage(), res.cause());
                }
            });
            await(countDownLatch);
        } else {
            vertx = Vertx.vertx(options);
        }
        return this;
    }

    private void deploy(){
        CountDownLatch countDownLatch = new CountDownLatch(1);

        Handler handler = stringAsyncResult -> {
            countDownLatch.countDown();
        };

        String verticleID = GuiceVerticleFactory.PREFIX + ":" + Gverticle.class.getName();
        runner = vertex -> {
            try {
                if (deploymentOptions != null) {
                    vertex.deployVerticle(verticleID, deploymentOptions, handler);
                } else {
                    vertex.deployVerticle(verticleID, handler);
                }
            } catch (Throwable t) {
                log.error(t.getMessage(), t);
            }
        };
        runner.accept(vertx);
        await(countDownLatch);
    }

    private void await(CountDownLatch mCountDownLatch) {
        try {
            mCountDownLatch.await();
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }


}
