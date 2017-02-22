package controllers;

import com.google.inject.Singleton;
import com.gvertx.core.models.Result;
import com.gvertx.core.models.Results;
import com.gvertx.core.params.FilterWith;
import com.gvertx.core.params.Param;
import filters.TestFilter;
import models.CurrentRequest;
import models.ReqContext;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangziqing on 17/2/15.
 */
@FilterWith({TestFilter.class})
@Singleton
public class TestController {


    public Result hello(@Param("id") String id,
                        @Param("a") String a,
                        @Param("b") String b,
                        @Param("c") String c,
                        @Param("d") String d,
                        @Param("e") String e,
                        @Param("f") String f,
                        @Param("g") String g,
                        @Param("h") String h,
                        @Param("i") String i) {
//        System.out.println(++k);
        Map map = new HashMap<>();
        map.put("id",id);
        return Results.ok().json().render(map);
    }


    public Result tt(@CurrentRequest ReqContext name) {

        return null;
    }



    public class User{
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
