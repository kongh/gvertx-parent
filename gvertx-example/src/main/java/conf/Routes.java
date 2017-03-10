package conf;

import com.google.inject.Inject;
import com.gvertx.core.ApplicationRoutes;
import com.gvertx.core.Grouter;
import controllers.TestController;

/**
 * Created by wangziqing on 17/2/15.
 */
public class Routes implements ApplicationRoutes {

    @Inject
    private TestController testController;

    @Override
    public void init(Grouter router) {
        router.POST().route("/1.json").with(TestController.class, "hello");
//        router.GET().route("/t.html").with(TestController.class, "index");
//
//        router.GET().route("/test.json").with(testController::hello);
    }
}
