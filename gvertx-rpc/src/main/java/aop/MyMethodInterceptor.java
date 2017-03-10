package aop;


import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * Created by wangziqing on 17/3/9.
 */
public class MyMethodInterceptor implements MethodInterceptor {

    public static void main(String[] args) {
//        FastClass.create()

    }
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        Object object = methodProxy.invokeSuper(o, objects);
        return object;
    }
}
