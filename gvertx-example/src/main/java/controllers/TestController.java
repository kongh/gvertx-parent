package controllers;

import com.google.inject.Singleton;
import com.gvertx.web.models.Context;
import com.gvertx.web.models.Result;
import com.gvertx.web.models.Results;
import com.gvertx.web.params.FilterWith;
import com.gvertx.web.params.Param;
import filters.TestFilter;
import rx.Observable;

/**
 * Created by wangziqing on 17/2/15.
 */
@FilterWith({TestFilter.class})
@Singleton
public class TestController {

    public Result hello(@Param("name") String name, Context context) throws InterruptedException {
        return Results.json().render(Observable.just("hello world"));
    }

}
