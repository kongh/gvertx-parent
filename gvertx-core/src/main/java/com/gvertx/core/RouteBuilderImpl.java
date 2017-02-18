package com.gvertx.core;

import com.esotericsoftware.reflectasm.MethodAccess;
import com.gvertx.core.constant.HttpConstant;
import com.gvertx.core.models.Result;
import com.gvertx.core.models.RouterRegister;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangziqing on 17/2/16.
 */
public class RouteBuilderImpl implements RouteBuilder{

    static final private Logger log = LoggerFactory.getLogger(RouteBuilderImpl.class);

    private String path;
    private HttpConstant.Method httpMethod;
    private Class<?> controller;

    private Method invokeMethod;


    public RouteBuilderImpl(HttpConstant.Method httpMethod) {
        this.httpMethod = httpMethod;
    }

    public RouteBuilder route(String path){
        this.path = path;
        return this;
    }
    @Override
    public void with(Class<?> controller, String s) {
        this.invokeMethod = verifyControllerMethod(controller,s);
        this.controller = controller;
    }


    static private final Map<Class<?>, MethodAccess> accessMap = new HashMap<>();
    public RouterRegister build(){
        return new RouterRegister(
                path, httpMethod,
                accessMap.computeIfAbsent(controller, c -> MethodAccess.get(controller)),
                invokeMethod, controller);
    }

    private Method verifyControllerMethod(Class<?> controllerClass,
                                          String controllerMethod) {
        Method methodFromQueryingClass = null;
        try {
            // 1. Make sure method is in class
            // 2. Make sure only one method is there. Otherwise we cannot really
            // know what to do with the parameters.
            for (Method method : controllerClass.getMethods()) {
                if (method.getName().equals(controllerMethod)) {
                    if (methodFromQueryingClass == null) {
                        methodFromQueryingClass = method;
                    } else {
                        throw new NoSuchMethodException();
                    }
                }
            }

            if (methodFromQueryingClass == null) {
                throw new NoSuchMethodException();
            }

            // make sure that the return type of that controller method
            // is of type Result.
            if (!Result.class.isAssignableFrom(methodFromQueryingClass.getReturnType())) {
                throw new NoSuchMethodException();
            }

        } catch (SecurityException e) {
            log.error(
                    "Error while checking for valid Controller / controllerMethod combination",
                    e);
        } catch (NoSuchMethodException e) {
            log.error("Error in route configuration!!!");
            log.error("Can not find Controller " + controllerClass.getName()
                    + " and method " + controllerMethod);
            log.error("Hint: make sure the controller returns a ninja.Result!");
            log.error("Hint: Ninja does not allow more than one method with the same name!");
        } finally {
            return methodFromQueryingClass;
        }
    }
}
