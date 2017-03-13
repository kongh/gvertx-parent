package com.gvertx.run;

import com.gvertx.annotation.Instance;
import com.gvertx.guice.GuiceVerticleFactory;
import com.gvertx.guice.GuiceVerticleLoader;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;


/**
 * Created by wangziqing on 17/3/12.
 */
public class Runner {
    static final private Logger log = LoggerFactory.getLogger(Runner.class);
    static final private String restVerticle = "conf.RestVerticle";

    public static final int DEFAULT_EVENT_LOOP_POOL_SIZE = 2 * Runtime.getRuntime().availableProcessors();


    private Vertx vertx;
    private DeploymentOptions deploymentOptions;
    private VertxOptions options;

    private String[] verticles;

    public static void main(String[] args) {
        new Runner().run();
    }

    public void run(){ this.init().deploy();}
    public void run(String ... verticles){
        this.verticles = verticles;
        this.init().deploy();
    }

    public void run(Class<? extends Verticle> ... verticles){
        if(null != verticles){
            this.verticles = new String[verticles.length];
            for(int i=0;i<verticles.length;i++){
                this.verticles[i] = verticles[i].getName();
            }
        }
        this.init().deploy();
    }
    private Runner init() {
        options = new VertxOptions();
        deploymentOptions = new DeploymentOptions();

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

    public static final String webModule = "com.gvertx.web.RestModule";
    private void deploy() {
        if(null != verticles){
            for(String verticle : verticles){
                deployVerticle(verticle);
            }
        }else{
            deployVerticle(restVerticle);
        }
    }

    private void deployVerticle(String verticle){
        Class cls;
        if (null != (cls = doesClassExist(verticle))) {
            deploymentOptions.setInstances(getInstance(cls, Instance.class,
                    "val").orElseGet(() -> DEFAULT_EVENT_LOOP_POOL_SIZE));

            deploymentOptions.setConfig(new JsonObject()
                    .put(GuiceVerticleLoader.MODULES, new JsonArray().add(webModule)));
            vertx.deployVerticle(GuiceVerticleFactory.PREFIX + ":" + verticle, deploymentOptions);
        }
    }

    private void await(CountDownLatch mCountDownLatch) {
        try {
            mCountDownLatch.await();
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }

    private Class doesClassExist(String nameWithPackage) {
        Class aClass = null;
        try {
            aClass = Class.forName(nameWithPackage, false, this.getClass().getClassLoader());
        } catch (ClassNotFoundException e) {
        }
        return aClass;
    }

    private Optional<Integer> getInstance(Class cls, Class annotationClasss, String annotationField) {
        Annotation annotation = cls.getAnnotation(annotationClasss);
        try {
            Method m = annotation.getClass().getDeclaredMethod(annotationField);
            m.setAccessible(true);
            Object obj;
            if (null != (obj = m.invoke(annotation))) {
                return Optional.ofNullable(Integer.valueOf(obj.toString()));
            }
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(null);
    }

}
