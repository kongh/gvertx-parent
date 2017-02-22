package com.gvertx.core.models;

import com.esotericsoftware.reflectasm.MethodAccess;
import com.gvertx.core.params.ArgumentExtractor;

/**
 * Created by wangziqing on 17/2/20.
 */
public class EndModel {
    private Object[] parameterObjs;
    private String[] paramNames;
    private String[] pathParamNames;
    private byte parameterSize;
    private Object invokeObj;

    private MethodAccess methodAccess;
    private String methodName;
    private ArgumentExtractor[] argumentExtractors;

    public ArgumentExtractor[] getArgumentExtractors() {
        return argumentExtractors;
    }

    public void setArgumentExtractors(ArgumentExtractor[] argumentExtractors) {
        this.argumentExtractors = argumentExtractors;
    }

    public Object[] getParameterObjs() {
        return parameterObjs;
    }

    public void setParameterObjs(Object[] parameterObjs) {
        this.parameterObjs = parameterObjs;
    }


    public String[] getParamNames() {
        return paramNames;
    }

    public void setParamNames(String[] paramNames) {
        this.paramNames = paramNames;
    }

    public String[] getPathParamNames() {
        return pathParamNames;
    }

    public void setPathParamNames(String[] pathParamNames) {
        this.pathParamNames = pathParamNames;
    }

    public byte getParameterSize() {
        return parameterSize;
    }

    public void setParameterSize(byte parameterSize) {
        this.parameterSize = parameterSize;
    }

    public Object getInvokeObj() {
        return invokeObj;
    }

    public void setInvokeObj(Object invokeObj) {
        this.invokeObj = invokeObj;
    }

    public MethodAccess getMethodAccess() {
        return methodAccess;
    }

    public void setMethodAccess(MethodAccess methodAccess) {
        this.methodAccess = methodAccess;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
}
