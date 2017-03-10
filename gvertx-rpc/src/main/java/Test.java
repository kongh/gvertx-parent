import com.acme.rxjava.service.test.TestServiceImpl;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import rpc.RPCServer;
import rpc.impl.RPCServerOptions;
import rpc.impl.VertxRPCServer;

/**
 * Created by wangziqing on 17/3/3.
 */
public class Test extends AbstractVerticle {

    public static void main(String[] args) {
//        ClusterManager mgr = new ZookeeperClusterManager();
        VertxOptions options = new VertxOptions();
        Vertx.clusteredVertx(options, res -> {
            if (res.succeeded()) {
                Vertx vertx = res.result();
                System.out.println(vertx);
            } else {
                System.out.println(res);
                // failed!
            }
        });
//        ClusterManager mgr = new ZookeeperClusterManager();
//        VertxOptions options = new VertxOptions().setClusterManager(mgr);
//        Vertx.clusteredVertx(options, res -> {
//            if (res.succeeded()) {
//                Vertx vertx = res.result();
//                System.out.println(vertx);
//            } else {
//                // failed!
//            }
//        });

//        ClusterManager mgr = new ZookeeperClusterManager();
//        ClusterManager mgr = new ZookeeperClusterManager();
//        VertxOptions options = new VertxOptions();
////        Vertx.vertx(options).deployVerticle(Test.class.getName());
//
//        Vertx.clusteredVertx(options, vertxAsyncResult -> {
//            if (vertxAsyncResult.succeeded()) {
//                vertxAsyncResult.result().deployVerticle(Test.class.getName(),stringAsyncResult -> {
//
//                });
//            }
//        });

    }


    @Override
    public void start() throws Exception {
        RPCServerOptions serverOption = new RPCServerOptions(vertx).setBusAddress("aa").addService(new TestServiceImpl());
        RPCServer rpcServer = new VertxRPCServer(serverOption);
//        TestService service = new TestServiceImpl();
//        System.out.println(service.getClass().getName());
//        System.out.println(service.getClass().getSimpleName());
//// Register the handler
//        ProxyHelper.registerService(TestService.class, getVertx(), service,
//                "aa");

//        vertx.eventBus().consumer("aa", message -> {
//            System.out.println(message);
//        });
//        MetricsService service = MetricsService.create(vertx);
//        JsonObject metrics = service.getMetricsSnapshot(vertx.eventBus());
//        JsonObject handlers = metrics.getJsonObject("handlers");
//        System.out.println("handlers:" + handlers.toString());
//
//        JsonObject message = service.getMetricsSnapshot("vertx.eventbus.message");
//        System.out.println(message);
    }
}
