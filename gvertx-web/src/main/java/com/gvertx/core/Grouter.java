package com.gvertx.core;

import io.vertx.rxjava.ext.web.Router;

/**
 * Created by wangziqing on 17/2/16.
 */
public interface Grouter {
    //从服务器上获取一个具体的资源或者一个资源列表
    RouteBuilderImpl GET();
    //在服务器上创建一个新的资源
    RouteBuilderImpl POST();
    //以整体的方式更新服务器上的一个资源
    RouteBuilderImpl PUT();
    //只更新服务器上一个资源的一个属性
    RouteBuilderImpl PATCH();
    //删除服务器上的一个资源
    RouteBuilderImpl DELETE();
    //获取一个资源的元数据，如数据的哈希值或最后的更新时间
    RouteBuilderImpl HEAD();
    //获取客户端能对资源做什么操作的信息
    RouteBuilderImpl OPTIONS();

    void compileRoutes(Router router);
}
