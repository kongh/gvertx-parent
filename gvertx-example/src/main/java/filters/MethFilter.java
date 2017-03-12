package filters;

import com.gvertx.web.models.Context;
import com.gvertx.web.models.Results;
import com.gvertx.web.params.Filter;
import com.gvertx.web.params.FilterChain;
import rx.Observable;

/**
 * Created by wangziqing on 17/2/22.
 */
public class MethFilter implements Filter {
    @Override
    public void filter(FilterChain filterChain, Context context) {
        filterChain.next(context);
        filterChain.end(context, Results.ok().json().render(Observable.just("w")));
    }


}
