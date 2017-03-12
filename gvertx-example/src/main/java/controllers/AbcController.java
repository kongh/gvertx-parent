package controllers;

import com.google.inject.Singleton;
import com.gvertx.web.models.Result;
import com.gvertx.web.params.FilterWith;
import filters.TestFilter;
import models.CurrentRequest;

/**
 * Created by wangziqing on 17/2/19.
 */
@FilterWith(TestFilter.class)
@Singleton
public class AbcController {

    public Result tt(@CurrentRequest(signatured = false) String name) {

        return null;
    }


}
