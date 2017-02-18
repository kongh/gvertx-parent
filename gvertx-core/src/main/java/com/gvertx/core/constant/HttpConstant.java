package com.gvertx.core.constant;

/**
 * Created by wangziqing on 17/2/18.
 */
public interface HttpConstant {
    enum  Method{
        POST("post"),
        GET("get"),
        PUT("put"),
        DELETE("delete");

        private String method;
        Method(String method){
            this.method = method;
        }

        public String getMethod() {
            return method;
        }
    }
}
