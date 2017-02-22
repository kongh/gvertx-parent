package filters;

import com.gvertx.core.models.Context;
import com.gvertx.core.params.Filter;
import com.gvertx.core.params.FilterChain;

/**
 * Created by wangziqing on 17/2/20.
 */
public class TestFilter implements Filter {

    @Override
    public void filter(FilterChain filterChain, Context context) {
        System.out.println("filter");
        filterChain.next(context);
    }
}
