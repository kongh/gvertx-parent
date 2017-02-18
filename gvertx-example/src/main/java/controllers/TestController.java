package controllers;

import com.google.inject.Singleton;
import com.gvertx.core.models.Result;
import com.gvertx.core.models.Results;
import com.gvertx.core.params.Param;
import models.CurrentRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangziqing on 17/2/15.
 */
@Singleton
public class TestController {

    int k = 0;
    public Result hello(@Param("id") String id,  @CurrentRequest(authenticated = false,signatured = false) String name) {
//        System.out.println(++k);
        List<User> list = new ArrayList<>();
        for(int i=0;i<1000;i++){
            User userd = new User();
            userd.setName("wwwwwwww:"+i);
            userd.setPass(i+"");
            list.add(userd);
        }
        return Results.ok().json().render(name).render(list);
    }

    public Result tt( @CurrentRequest(signatured = false) String name) {
        System.out.println(++k);
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
