package filters;

import com.gvertx.web.models.Context;
import com.gvertx.web.params.Filter;
import com.gvertx.web.params.FilterChain;

/**
 * Created by wangziqing on 17/2/20.
 */
public class TestFilter implements Filter {

    @Override
    public void filter(FilterChain filterChain, Context context) {
        filterChain.next(context);
    }
}
