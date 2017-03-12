package com.gvertx.web.constant;

/**
 * Created by wangziqing on 17/2/18.
 */
public interface HttpConstant {
    enum  Method{
        POST("POST"),
        GET("GET"),
        PUT("PUT"),
        PATCH("PATCH"),
        DELETE("DELETE"),
        OPTIONS("OPTIONS"),
        HEAD("HEAD");

        private String method;
        Method(String method){
            this.method = method;
        }

        public String getMethod() {
            return method;
        }
    }
}
