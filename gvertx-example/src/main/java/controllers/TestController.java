package controllers;

import com.google.inject.Singleton;
import com.gvertx.core.models.Context;
import com.gvertx.core.models.Result;
import com.gvertx.core.models.Results;
import com.gvertx.core.params.FilterWith;
import com.gvertx.core.params.Param;
import filters.TestFilter;
import io.vertx.core.json.JsonObject;
import rx.Observable;

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
    public Result hello(@Param("name") String name,@Param("ss") String ss, Context context) throws InterruptedException {
        System.out.println(context.getRoutingContext().getBodyAsString());
//        Long s = context.getRoutingContext().request();
//        System.out.println(s);
        return Results.json().render(Observable.just(1));
    }

    public Result index(Context context) {
        context.getRoutingContext().put("hello","wangziqing");
        JsonObject jsonObject = new JsonObject();
        return Results.html().render(Observable.just(jsonObject.put("name", "thymeleaf!")));
    }

    public Result test() {
        return Results.html().render(Observable.just(new JsonObject().put("name", "thymeleaf!")));
    }
    public static void main(String[] args) {
        JsonObject jsonObject = new JsonObject();

        System.out.println(jsonObject instanceof Map);
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
