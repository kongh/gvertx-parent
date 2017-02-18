package com.gvertx.core.models;

import com.esotericsoftware.reflectasm.MethodAccess;
import com.gvertx.core.constant.HttpConstant;

import java.lang.reflect.Method;

/**
 * Created by wangziqing on 17/2/16.
 */
public class RouterRegister {
    private String path;
    private HttpConstant.Method httpMethod;
    private MethodAccess methodAccess;
    private Method invokeMethod;
    private Class<?> cls;

    public RouterRegister(String path,
                          HttpConstant.Method httpMethod ,
                          MethodAccess methodAccess,
                          Method invokeMethod,Class<?> cls) {
        this.path = path;
        this.methodAccess = methodAccess;
        this.invokeMethod = invokeMethod;
        this.httpMethod = httpMethod;
        this.cls = cls;
    }

    public String getPath() {
        return path;
    }

    public HttpConstant.Method getHttpMethod() {
        return httpMethod;
    }

    public MethodAccess getMethodAccess() {
        return methodAccess;
    }

    public Method getInvokeMethod() {
        return invokeMethod;
    }

    public Class<?> getCls() {
        return cls;
    }

    @Override
    public String toString() {
        return "RouterRegister{" +
                "path='" + path + '\'' +
                ", httpMethod='" + httpMethod + '\'' +
                ", methodAccess=" + methodAccess +
                ", invokeMethod=" + invokeMethod +
                ", cls=" + cls +
                '}';
    }
}
