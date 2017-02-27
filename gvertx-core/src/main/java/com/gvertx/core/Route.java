/**
 * Copyright (C) 2012-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gvertx.core;

import com.esotericsoftware.reflectasm.MethodAccess;
import com.gvertx.core.params.ArgumentExtractor;

/**
 * A route
 */
public class Route {
    private Object invokeObj;
    private MethodAccess methodAccess;
    private String methodName;
    private ArgumentExtractor[] argumentExtractors;

    private boolean isBlock;

    public Object invoke(Object[] parameters){
        return this.methodAccess.invoke(this.invokeObj,
                this.methodName,
                parameters);
    }

    public Route(Object invokeObj,MethodAccess methodAccess,String methodName,ArgumentExtractor[] argumentExtractors,
                 boolean isBlock){
        this.invokeObj = invokeObj;
        this.methodAccess = methodAccess;
        this.methodName = methodName;
        this.argumentExtractors = argumentExtractors;
        this.isBlock = isBlock;
    }

    public ArgumentExtractor[] getArgumentExtractors() {
        return argumentExtractors;
    }

    public void setArgumentExtractors(ArgumentExtractor[] argumentExtractors) {
        this.argumentExtractors = argumentExtractors;
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

    public boolean getIsBlock() {
        return isBlock;
    }
}
