package filters;

import com.google.inject.Inject;
import com.gvertx.core.models.Context;
import com.gvertx.core.params.Filter;
import com.gvertx.core.params.FilterChain;
import io.vertx.rxjava.core.Vertx;

/**
 * Created by wangziqing on 17/2/20.
 */
public class TestFilter implements Filter {

    @Override
    public void filter(FilterChain filterChain, Context context) {
        filterChain.next(context);
    }
}
