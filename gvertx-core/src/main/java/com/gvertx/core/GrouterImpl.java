package com.gvertx.core;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.gvertx.core.constant.HttpConstant;
import com.gvertx.core.models.Context;
import com.gvertx.core.models.EndModel;
import com.gvertx.core.models.RouterRegister;
import com.gvertx.core.params.*;
import io.vertx.rxjava.ext.web.Route;
import io.vertx.rxjava.ext.web.Router;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by wangziqing on 17/2/16.
 */
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


    static private final Map<Class<?>, LinkedList<Class<? extends Filter>>> filterMap = new HashMap<>();
    static private final Map<Class<? extends Annotation>, Annotation> annotationBinds = new HashMap<>();

    @Override
    public void compileRoutes(Router router) {
        if (isCompiled) {
            throw new IllegalStateException("Routes already compiled");
        }
        isCompiled = true;

        List<Xrouter> xrouters = new ArrayList(allRouteBuilders.size());
        List<EndModel> models = new ArrayList(allRouteBuilders.size());

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
                case PATCH:
                    ret = router.patch(rrg.getPath());
                    break;
                case HEAD:
                    ret = router.head(rrg.getPath());
                    break;
                case OPTIONS:
                    ret = router.options(rrg.getPath());
                    break;
            }
            LinkedList<Class<? extends Filter>> filters;
            if (null == (filters = filterMap.get(rrg.getCls()))) {
                filters = new LinkedList<>(calculateFiltersForClass(rrg.getCls()));
            }
            xrouters.add(new Xrouter(ret, buildModel(rrg, models), filters));
        }

        Injector ijct = injector.createChildInjector(new AbstractModule() {
            @Override
            protected void configure() {
                for (Map.Entry<Class<? extends Annotation>, Annotation> entry : annotationBinds.entrySet()) {
                    bind((Class<Annotation>) entry.getKey()).toInstance(entry.getValue());
                }
            }
        });
        for (EndModel model : models) {
            Object[] objects = model.getParameterObjs();
            for (int i = 0, len = objects.length; i < len; i++) {
                Object obj = objects[i];
                if (null != obj) {
                    if (obj instanceof Class) {
                        if (obj.getClass().isInstance(ArgumentExtractor.class)) {
                            ArgumentExtractor argumentExtractor = (ArgumentExtractor) ijct.getInstance((Class) obj);
                            model.getArgumentExtractors()[i] = argumentExtractor;
                        }

                    }
                }
            }
        }
        for (Xrouter xrouter : xrouters) {
            int lastindex = -1;
            EndModel endModel = xrouter.getEndModel();
            for (int i = endModel.getParameterSize() - 1; i >= 0; i--) {
                if (null != endModel.getArgumentExtractors()[i]) {
                    lastindex = i;
                    break;
                }
            }
            FilterChain filterChain = buildFilterChain(xrouter.getFilters(), endModel,
                    buildExtractorChain(endModel.getArgumentExtractors(), 0, lastindex, endModel));

            xrouter.route.handler(routingContext -> filterChain.next(new Context(routingContext)));
        }
        System.out.println("router处理耗时:" + (System.currentTimeMillis() - s));
    }


    public ArgumentExtractorChain buildExtractorChain(ArgumentExtractor[] extractors, int index, final int lastindex, EndModel endModel) {
        if (lastindex == -1 || index > lastindex) {
            return new ArgumentExtractorEndChainImpl(endModel, --index);
        } else {
            ArgumentExtractor extractor = extractors[index];
            if (null == extractor) {
                for (; index <= lastindex; index++) {
                    if (null != (extractor = extractors[index])) {
                        break;
                    }
                }
            }
            return new ArgumentExtractorChainImpl(extractor, endModel, index - 1,
                    buildExtractorChain(extractors, ++index, lastindex, endModel));

        }
    }

    private FilterChain buildFilterChain(LinkedList<Class<? extends Filter>> filters, EndModel endModel, ArgumentExtractorChain extractorChain) {
        if (filters.isEmpty()) {
            return new FilterChainEndImpl(extractorChain);
        } else {
            Class<? extends Filter> filter = filters.pop();

            return new FilterChainImpl(injector.getProvider(filter),
                    buildFilterChain(filters, endModel, extractorChain));

        }
    }


    private Set<Class<? extends Filter>> calculateFiltersForClass(Class controller) {
        LinkedHashSet<Class<? extends Filter>> filters = new LinkedHashSet<>();

        if (controller.getSuperclass() != null) {
            filters.addAll(calculateFiltersForClass(controller.getSuperclass()));
        }
        if (controller.getInterfaces() != null) {
            for (Class clazz : controller.getInterfaces()) {
                filters.addAll(calculateFiltersForClass(clazz));
            }
        }
        FilterWith filterWith = (FilterWith) controller
                .getAnnotation(FilterWith.class);
        if (filterWith != null) {
            filters.addAll(Arrays.asList(filterWith.value()));
        }
        return filters;
    }


    private EndModel buildModel(RouterRegister rrg, List<EndModel> models) {
        Method method = rrg.getInvokeMethod();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        Class[] parameterTypes = method.getParameterTypes();

        byte parameterSize = new Integer(parameterTypes.length).byteValue();
        Object[] parameterObjs = new Object[parameterSize];
        ArgumentExtractor[] argumentExtractors = new ArgumentExtractor[parameterSize];
        String[] paramNames = new String[parameterSize];
        String[] pathParamNames = new String[parameterSize];

        for (int i = 0, len = parameterAnnotations.length; i < len; i++) {
            for (Annotation annotation : parameterAnnotations[i]) {

                WithArgumentExtractor withArgumentExtractor = annotation.annotationType()
                        .getAnnotation(WithArgumentExtractor.class);
                if (null != withArgumentExtractor) {

//                    argumentExtractors[i] = injector.createChildInjector(new AbstractModule() {
//                        @Override
//                        protected void configure() {
//                            bind((Class<Annotation>) annotation.annotationType()).toInstance(annotation);
//                        }
//                    }).getInstance(withArgumentExtractor.value());

                    argumentExtractors[i] = instantiateComponent(withArgumentExtractor.value(),annotation,parameterTypes[i],injector);

                    annotationBinds.put(annotation.annotationType(), annotation);
                    parameterObjs[i] = withArgumentExtractor.value();
                }

            }
        }

        Object object = injector.getInstance(rrg.getCls());
        EndModel endModel = new EndModel();
        endModel.setInvokeObj(object);
        endModel.setParameterObjs(parameterObjs);
        endModel.setParameterSize(parameterSize);
        endModel.setParamNames(paramNames);
        endModel.setPathParamNames(pathParamNames);
        endModel.setMethodName(rrg.getInvokeMethod().getName());
        endModel.setMethodAccess(rrg.getMethodAccess());
        endModel.setArgumentExtractors(argumentExtractors);
        models.add(endModel);
        return endModel;
    }


    private static <T> T instantiateComponent(Class<? extends T> argumentExtractor,
                                              final Annotation annotation, final Class<?> paramType,
                                              Injector injector) {
        // Noarg constructor
        Constructor noarg = getNoArgConstructor(argumentExtractor);
        if (noarg != null) {
            try {
                return (T) noarg.newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        // Simple case, just takes the annotation
        Constructor simple = getSingleArgConstructor(argumentExtractor, annotation.annotationType());
        if (simple != null) {
            try {
                return (T) simple.newInstance(annotation);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        // Simple case, just takes the parsed class
        Constructor simpleClass = getSingleArgConstructor(argumentExtractor, Class.class);
        if (simpleClass != null) {
            try {
                return (T) simpleClass.newInstance(paramType);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        // Complex case, use Guice.  Create a child injector with the annotation in it.
        return injector.createChildInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind((Class<Annotation>) annotation.annotationType()).toInstance(annotation);
            }
        }).getInstance(argumentExtractor);
    }


    private static Constructor getNoArgConstructor(Class<?> clazz) {
        for (Constructor constructor : clazz.getConstructors()) {
            if (constructor.getParameterTypes().length == 0) {
                return constructor;
            }
        }
        return null;
    }

    private static Constructor getSingleArgConstructor(Class<?> clazz, Class<?> arg) {
        for (Constructor constructor : clazz.getConstructors()) {
            if (constructor.getParameterTypes().length == 1) {
                if (constructor.getParameterTypes()[0].isAssignableFrom(arg)) {
                    return constructor;
                }
            }
        }
        return null;
    }


    private class Xrouter {
        private Route route;
        private EndModel endModel;
        private LinkedList<Class<? extends Filter>> filters;

        public Xrouter(Route route, EndModel endModel, LinkedList<Class<? extends Filter>> filters) {
            this.route = route;
            this.endModel = endModel;
            this.filters = filters;
        }

        public Route getRoute() {
            return route;
        }

        public EndModel getEndModel() {
            return endModel;
        }

        public LinkedList<Class<? extends Filter>> getFilters() {
            return filters;
        }
    }
}
