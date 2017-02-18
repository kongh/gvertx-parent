package models;

import com.google.inject.Inject;
import com.gvertx.core.params.ArgumentExtractor;
import io.vertx.rxjava.ext.web.RoutingContext;

/**
 * Created by wangziqing on 17/2/18.
 */
public class ReqExtractor implements ArgumentExtractor<String> {

    private boolean a;
    private boolean b;
    @Inject
    public ReqExtractor(CurrentRequest currentRequest){
        a = currentRequest.authenticated();
        b = currentRequest.signatured();
//        System.out.println(currentRequest.authenticated());
//        System.out.println(currentRequest.signatured());
    }
    @Override
    public String extract(RoutingContext routingContext) {
        System.out.println(a+":"+b);
        return "hello extractor";
    }
}
