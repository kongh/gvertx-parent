package models;

import com.google.inject.Inject;
import com.gvertx.web.models.Context;
import com.gvertx.web.params.ArgumentExtractor;
import com.gvertx.web.params.ArgumentExtractorChain;

/**
 * Created by wangziqing on 17/2/18.
 */
public class ReqExtractor implements ArgumentExtractor<ReqContext> {

    private CurrentRequest currentRequest;


    @Inject
    public ReqExtractor(CurrentRequest currentRequest){
        this.currentRequest = currentRequest;
    }


    @Override
    public void extract(ArgumentExtractorChain<ReqContext> extractorChain, Context context) {
        System.out.println(currentRequest);
        extractorChain.next(context,new ReqContext("hello extractor"));
    }


}
