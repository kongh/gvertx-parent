package com.gvertx.core;

import com.google.inject.Injector;
import com.gvertx.core.constant.HttpConstant;
import com.gvertx.core.models.Result;
import com.gvertx.core.params.*;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.rxjava.core.Vertx;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * Created by wangziqing on 17/2/16.
 */
public class RouteBuilderImpl implements RouteBuilder {

    static final private Logger log = LoggerFactory.getLogger(RouteBuilderImpl.class);

    private String path;
    private HttpConstant.Method httpMethod;
    private FilterChain filterChain;
    private Object[] parameters;
    private Class controller;
    private Method controllerMethod;

    private boolean isBlock;

    public RouteBuilderImpl(HttpConstant.Method httpMethod) {
        this.httpMethod = httpMethod;
    }

    public RouteBuilder route(String path) {
        this.path = path;
        return this;
    }

    @Override
    public void with(Class<?> controller, String s) {
        this.controllerMethod = verifyThatControllerAndMethodExists(controller, s, isBlock);
        this.controller = controller;
    }

    @Override
    public void blockingWith(Class<?> controller, String s) {
        this.isBlock = true;
        this.controllerMethod = verifyThatControllerAndMethodExists(controller, s, isBlock);
        this.controller = controller;
    }

    public RouteBuilderImpl buildRoute(Injector injector) {
        if (controller == null) {
            log.error("Error in route configuration for {}", path);
            throw new IllegalStateException("Route not with a controller or result");
        }
        if (null == controllerMethod) {
            return null;
        }
        LinkedList<Class<? extends Filter>> filters = new LinkedList<Class<? extends Filter>>();

        filters.addAll(calculateFiltersForClass(controller));
        FilterWith filterWith = controllerMethod.getAnnotation(FilterWith.class);
        if (filterWith != null) {
            filters.addAll(Arrays.asList(filterWith.value()));
        }
        Route route = ArgumentExtractors.build(controller, controllerMethod, isBlock, injector);

        int lastindex = -1;
        for (int i = route.getArgumentExtractors().length - 1; i >= 0; i--) {
            if (null != route.getArgumentExtractors()[i]) {
                lastindex = i;
                break;
            }
        }

        this.filterChain = buildFilterChain(
                filters,
                buildExtractorChain(route.getArgumentExtractors(), 0, lastindex, route, injector.getInstance(Vertx.class)),
                injector);

        this.parameters = new Object[route.getArgumentExtractors().length];
        return this;
    }

    static private ArgumentExtractorChain buildExtractorChain(ArgumentExtractor[] extractors,
                                                              int index,
                                                              final int lastindex,
                                                              Route route, Vertx vertx) {
        if (lastindex == -1 || index > lastindex) {
            return new ArgumentExtractorEndChainImpl(route, --index, vertx);
        } else {
            ArgumentExtractor extractor = extractors[index];
            if (null == extractor) {
                for (; index <= lastindex; index++) {
                    if (null != (extractor = extractors[index])) {
                        break;
                    }
                }
            }
            return new ArgumentExtractorChainImpl(extractor, index - 1,
                    buildExtractorChain(extractors, ++index, lastindex, route, vertx) , vertx);
        }
    }

    static private FilterChain buildFilterChain(LinkedList<Class<? extends Filter>> filters,
                                                ArgumentExtractorChain extractorChain,
                                                Injector injector) {
        if (filters.isEmpty()) {
            return new FilterChainEndImpl(extractorChain,injector.getInstance(Vertx.class));
        } else {
            Class<? extends Filter> filter = filters.pop();

            return new FilterChainImpl(injector.getProvider(filter),
                    buildFilterChain(filters, extractorChain, injector),
                    injector.getInstance(Vertx.class));
        }
    }


    static private Set<Class<? extends Filter>> calculateFiltersForClass(Class controller) {
        LinkedHashSet<Class<? extends Filter>> filters = new LinkedHashSet<Class<? extends Filter>>();
        // First step up the superclass tree, so that superclass filters come
        // first
        // Superclass
        if (controller.getSuperclass() != null) {
            filters.addAll(calculateFiltersForClass(controller.getSuperclass()));
        }
        // Interfaces
        if (controller.getInterfaces() != null) {
            for (Class clazz : controller.getInterfaces()) {
                filters.addAll(calculateFiltersForClass(clazz));
            }
        }
        // Now add from here
        FilterWith filterWith = (FilterWith) controller
                .getAnnotation(FilterWith.class);
        if (filterWith != null) {
            filters.addAll(Arrays.asList(filterWith.value()));
        }
        // And return
        return filters;
    }


    static private Method verifyThatControllerAndMethodExists(Class controller,
                                                              String controllerMethod, boolean isBlock) {
        try {

            Method methodFromQueryingClass = null;

            // 1. Make sure method is in class
            // 2. Make sure only one method is there. Otherwise we cannot really
            // know what
            // to do with the parameters.
            for (Method method : controller.getMethods()) {
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
            if (methodFromQueryingClass.getReturnType().isAssignableFrom(
                    Result.class)) {
                return methodFromQueryingClass;
            } else {
                throw new NoSuchMethodException();
            }
//            if (methodFromQueryingClass.getReturnType().isAssignableFrom(
//                    Observable.class)){
//                Type returnType = methodFromQueryingClass.getGenericReturnType();
//                if(returnType instanceof ParameterizedType){
//                    Type[] typeArguments = ((ParameterizedType) returnType).getActualTypeArguments();
//                    if(typeArguments.length == 1 && ((Class) typeArguments[0]).isAssignableFrom(Result.class)){
//                        return methodFromQueryingClass;
//                    }
//                }
//                throw new NoSuchMethodException();
//            }


        } catch (SecurityException e) {
            log.error(
                    "Error while checking for valid Controller / controllerMethod combination",
                    e);
        } catch (NoSuchMethodException e) {

            log.error("Error in route configuration!!!");
            log.error("Can not find Controller " + controller.getName()
                    + " and method " + controllerMethod);
            log.error("Hint: make sure the controller returns a ninja.Result!");
            log.error("Hint: Ninja does not allow more than one method with the same name!");
        }
        return null;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public String getPath() {
        return path;
    }

    public HttpConstant.Method getHttpMethod() {
        return httpMethod;
    }

    public FilterChain getFilterChain() {
        return filterChain;
    }

}
