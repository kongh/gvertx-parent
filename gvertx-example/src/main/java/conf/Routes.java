package conf;

import com.gvertx.core.ApplicationRoutes;
import com.gvertx.core.Grouter;
import controllers.TestController;

/**
 * Created by wangziqing on 17/2/15.
 */
public class Routes implements ApplicationRoutes {

    @Override
    public void init(Grouter router) {
        router.GET().route("/1.json").with(TestController.class, "hello");
    }
}
