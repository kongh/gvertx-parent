package conf;

import com.google.inject.Inject;
import com.gvertx.core.ApplicationVertxRoutes;
import controllers.TestController;
import io.vertx.rxjava.ext.web.Router;

/**
 * Created by wangziqing on 17/2/15.
 */
public class Routes implements ApplicationVertxRoutes {

    @Inject
    private TestController testController;

    @Override
    public void init(Router router) {
        router.get("/").handler(testController::hello);
        System.out.println("init");
    }
}
