import com.acme.rxjava.service.test.TestService;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.rxjava.core.AbstractVerticle;
import rpc.impl.RPCClientOptions;
import rpc.impl.VertxRPCClient;

/**
 * Created by wangziqing on 17/3/3.
 */
public class Test2 extends AbstractVerticle {

    public static void main(String[] args) {
        VertxOptions options = new VertxOptions();
//        Vertx.vertx().deployVerticle(Test2.class.getName());
        Vertx.clusteredVertx(options, vertxAsyncResult -> {
            if (vertxAsyncResult.succeeded()) {
                vertxAsyncResult.result().deployVerticle(Test2.class.getName(),stringAsyncResult -> {

                });
            }
        });

    }
    @Override
    public void start() throws Exception {
        RPCClientOptions<TestService> rpcClientOptions = new RPCClientOptions<TestService>(getVertx()).setBusAddress("aa")
                .setServiceClass(TestService.class);
        TestService myService = new VertxRPCClient<>(rpcClientOptions).bindService();
        myService.save(null,jsonObjectAsyncResult -> {
            System.out.println(jsonObjectAsyncResult);
        });
//invoking service
//        myService.hello("world", result -> {
//            //TODO
//        });
//        TestService testService = ProxyHelper.createProxy(TestService.class, getVertx(), "aa");
//        System.out.println(testService);
//
//        testService.save(null,jsonObjectAsyncResult -> {
//            System.out.println(jsonObjectAsyncResult.succeeded());
//        });

    }
}
