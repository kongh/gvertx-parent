package models;

import com.google.inject.Inject;
import com.gvertx.core.models.Context;
import com.gvertx.core.params.ArgumentExtractor;
import com.gvertx.core.params.ArgumentExtractorChain;

import java.lang.annotation.Annotation;

/**
 * Created by wangziqing on 17/2/18.
 */
public class ReqExtractor2 implements ArgumentExtractor<ReqContext> {

    private boolean a;
    private boolean b;


    @Inject
    public ReqExtractor2(CurrentRequest2 currentRequest){
        a = currentRequest.authenticated();
        b = currentRequest.signatured();

      //  System.out.println(a+":"+b);
//        System.out.println(currentRequest.authenticated());
//        System.out.println(currentRequest.signatured());
    }

    @Override
    public void extract(ArgumentExtractorChain<ReqContext> argumentExtractorChain, Context context) {
        System.out.println(argumentExtractorChain + ":extractor2->"+a+":"+b);
        argumentExtractorChain.next(context,new ReqContext("hello extractor2"));
    }


}
