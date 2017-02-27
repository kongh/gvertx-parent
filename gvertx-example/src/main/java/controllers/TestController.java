package controllers;

import com.google.inject.Singleton;
import com.gvertx.core.models.Result;
import com.gvertx.core.models.Results;
import com.gvertx.core.params.FilterWith;
import com.gvertx.core.params.Param;
import filters.TestFilter;
import rx.Observable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangziqing on 17/2/15.
 */
@FilterWith({TestFilter.class})
@Singleton
public class TestController {


    private String slowBlockingMethod() {
        try {
            System.out.println(String.format("subscribeOn:%s,activeCount:%s", Thread.currentThread().getName(), Thread.activeCount()));
            Thread.sleep(2000l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "aaa";
    }

    //@FilterWith(MethFilter.class)
    public Result hello(@Param("name") String name) throws InterruptedException {
        User user = new User();
        user.setPass("ww");
        user.setName("ee");
        Map m = new HashMap<>();
        m.put("ww","dd");
        m.put("e","e");
        return Results.json()
                .render(Observable.just("x1",false))
                .render(Observable.just(1))
                .render(Observable.just(m))
                .render(Observable.just(user));
    }



    public class User {
        private String name;
        private String pass;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPass() {
            return pass;
        }

        public void setPass(String pass) {
            this.pass = pass;
        }
    }
}
