/**
 * Copyright (C) 2012-2017 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gvertx.core.params;

import com.esotericsoftware.reflectasm.MethodAccess;
import com.google.common.collect.ImmutableMap;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.gvertx.core.Route;
import com.gvertx.core.extractors.ParamExtractor;
import com.gvertx.core.extractors.PathParamExtractor;
import com.gvertx.core.models.Context;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Optional;

/**
 * Invokes methods on the controller, extracting arguments out
 */
public class ArgumentExtractors {

    private static final Map<Class<?>, ArgumentExtractor<?>> STATIC_EXTRACTORS =
            ImmutableMap.<Class<?>, ArgumentExtractor<?>>builder()
                    .put(Context.class, new ContextExtractor())
                    .build();


    private void just(){

    }

    public void consumer(){
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
               System.out.println(Thread.currentThread().getName());
                subscriber.onNext("MultiThreading");
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
//                .observeOn(ArgumentExtractors.main();)
                .subscribe(s -> {
                    System.out.println(Thread.currentThread().getName()+":"+s);
                });

    }

    private Object slowBlockingMethod() {
        try {
            System.out.println(Thread.activeCount());
            Thread.sleep(2000l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("blocking method");
        return Observable.just("ww");
    }

    public Observable<Object> newMethod() {
        return Observable.defer(() -> Observable.just(slowBlockingMethod()));
    }

    public Observable controller(){

        newMethod().subscribe(result->{
            Observable.just("ww");
        },error->{

        });
        return null;
    }
    public static void main(String[] args) {

//        ArgumentExtractors a = new ArgumentExtractors();
//        a.newMethod().subscribe(result->{
//            System.out.println(Thread.activeCount());
//
//            System.out.println(result);
//        });
//        System.out.println("start");
//        System.out.println(Thread.activeCount());



//        ArgumentExtractors a = new ArgumentExtractors();
//        Observable.zip(
//                a.x1(),
//                a.x2(),
//                a.x2(),
//                (photo, metadata,x) -> Observable.just(""))
//                .subscribe(photoWithData -> System.out.println(photoWithData));
//
//        Observable x = Observable.never();
//        x.just("www");
////        Observable x = Observable.create((subscriber -> {
////            subscriber.onStart();
////        }));
//        x.flatMap(r->{
//            System.out.println("ww");
//
//            return Observable.just(r+":w");
//        }).subscribe(s -> {
//            System.out.println("#"+s);
//        });
//
//        Observable<String> observable = Observable.just("dd");
//
//        observable.subscribe(xx->{
//            System.out.println(xx);
//        },err->{
//            System.out.println(err);
//        },()->{
//            System.out.println("done");
//        });

//        CompletableFuture<String> completableFuture = CompletableFuture.completedFuture("ddd");
//        completableFuture.whenComplete((s, throwable) -> {
//            System.out.println(s);
//            completableFuture.complete("ww");
//        }).whenComplete((s, throwable) -> {
//            System.out.println(s);
//        });
//        Future future = Future.future();
//        future.setHandler(asyncResult -> {
//            System.out.println(1);
//           System.out.println(asyncResult);
//        });
//        Observable<String> observable = Observable.just("");
//        observable.subscribe(xx->{});
//        future.complete("ss");
    }
    public static class ContextExtractor implements ArgumentExtractor<Object> {
        @Override
        public void extract(ArgumentExtractorChain<Object> argumentExtractorChain, Context context) {
            argumentExtractorChain.next(context, context);
        }
    }


    static public Route build(Class controller,
                              Method functionalMethod,
                              boolean isBlock,
                              Injector injector) {
        // get both the parameters...
        final Type[] genericParameterTypes = functionalMethod.getGenericParameterTypes();
        final MethodParameter[] methodParameters = MethodParameter.convertIntoMethodParameters(genericParameterTypes);
        // ... and all annotations for the parameters
        final Annotation[][] paramAnnotations = functionalMethod
                .getParameterAnnotations();

        ArgumentExtractor<?>[] argumentExtractors = new ArgumentExtractor<?>[methodParameters.length];

        // now we skip through the parameters and process the annotations
        for (int i = 0; i < methodParameters.length; i++) {
            argumentExtractors[i] = getArgumentExtractor(methodParameters[i], paramAnnotations[i], injector);
        }

        return new Route(injector.getInstance(controller),
                MethodAccess.get(controller),
                functionalMethod.getName(),
                argumentExtractors,isBlock);
    }


    private static ArgumentExtractor<?> getArgumentExtractor(
            MethodParameter methodParameter,
            Annotation[] annotations, Injector injector) {

        ArgumentExtractor<?> extractor = STATIC_EXTRACTORS.get(methodParameter.parameterClass);

        if (extractor == null) {
            // See if we have a WithArgumentExtractor annotated annotation
            for (Annotation annotation : annotations) {
                if (annotation instanceof Param) {
                    extractor = new ParamExtractor(((Param) annotation).value(), methodParameter.parameterClass);
                    continue;
                } else if (annotation instanceof PathParam) {
                    extractor = new PathParamExtractor(((PathParam) annotation).value(), methodParameter.parameterClass);
                    continue;
                } else {
                    WithArgumentExtractor withArgumentExtractor = annotation.annotationType()
                            .getAnnotation(WithArgumentExtractor.class);
                    if (withArgumentExtractor != null) {
                        extractor = instantiateComponent(withArgumentExtractor.value(), annotation,
                                methodParameter.parameterClass, injector);
                        break;
                    }
                }
            }
        }

        return extractor;
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
//                bind(ArgumentClassHolder.class).toInstance(new ArgumentClassHolder(paramType));
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


    /**
     * Just a little helper that makes it possible to handle things like
     * myControllerMethod(@Param("param1") Optional<String> myValue)
     * <p>
     * It investigates the type parameter and allows to remember whether a type
     * was wrapped in an Optional or not. It stores the "real" type of the parameter
     * that the extractor should extract (String and not Optional in example above).
     */
    private static class MethodParameter {
        public boolean isOptional;
        public Class<?> parameterClass;

        private MethodParameter(Type genericType) {
            try {
                // a ParameterizedType is something like Optional<String> or List<String>...
                if (genericType instanceof ParameterizedType) {
                    ParameterizedType parameterizedType = (ParameterizedType) genericType;
                    Class<?> maybeOptional = getClass(parameterizedType.getRawType());

                    // The method expects an Optional, so we extract the first 
                    // generic which should determine the extractor that will be used to 
                    // extract the value;
                    if (maybeOptional.isAssignableFrom(Optional.class)) {
                        isOptional = true;
                        parameterClass = getClass(parameterizedType.getActualTypeArguments()[0]);
                    }
                }

                if (parameterClass == null) {
                    isOptional = false;
                    parameterClass = getClass(genericType);
                }
            } catch (Exception e) {
                throw new RuntimeException("Oops. Something went wrong while investigating method parameters for controller class invocation", e);
            }

        }

        public static MethodParameter[] convertIntoMethodParameters(Type[] genericParameterTypes) {
            MethodParameter[] methodParameters = new MethodParameter[genericParameterTypes.length];
            for (int i = 0; i < genericParameterTypes.length; i++) {
                methodParameters[i] = new MethodParameter(genericParameterTypes[i]);
            }
            return methodParameters;
        }

        private Class<?> getClass(Type type) {
            if (type instanceof Class) {
                return (Class<?>) type;
            } else {
                throw new RuntimeException(
                        "Oops. That's a strange internal Ninja error.\n"
                                + "Seems someone tried to convert a type into a class that is not a real class. ( " + type.getTypeName() + ")");
            }
        }
    }

}
