package filters;

import com.gvertx.core.models.Context;
import com.gvertx.core.models.Results;
import com.gvertx.core.params.Filter;
import com.gvertx.core.params.FilterChain;
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
