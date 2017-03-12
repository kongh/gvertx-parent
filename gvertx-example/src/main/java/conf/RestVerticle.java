package conf;

import com.gvertx.annotation.Instance;
import com.gvertx.run.Runner;
import com.gvertx.web.RestAbstractVerticle;
import controllers.TestController;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

/**
 * Created by wangziqing on 17/3/12.
 */
@Instance(val = 2)
public class RestVerticle extends RestAbstractVerticle {

    static final private Logger log = LoggerFactory.getLogger(RestVerticle.class);
    static int port = 8080;

    @Override
    public void start() {
        GET().route("/hello.json").with(TestController.class,"hello");

        vertx.createHttpServer()
                .requestHandler(router::accept)
                .rxListen(port)
                .subscribe(
                        server -> log.info(String.format("Server is now listening.  Thread:%s ", Thread.currentThread())),
                        failure -> log.info(String.format("Server could not start. Thread:%s", Thread.currentThread()), failure)
                );

    }

    public static void main(String[] args) {
        new Runner().run(RestVerticle.class);
    }


}
