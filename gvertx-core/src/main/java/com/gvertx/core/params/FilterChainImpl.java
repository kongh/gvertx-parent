/**
 * Copyright (C) 2012-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gvertx.core.params;

import com.google.inject.Provider;
import com.gvertx.core.ResultWrite;
import com.gvertx.core.models.Context;
import com.gvertx.core.models.Result;

/**
 * Implementation of the filter chain
 */
public class FilterChainImpl extends ResultWrite implements FilterChain {
    
    private final Provider<? extends Filter> filterProvider;
    private final FilterChain next;

    public FilterChainImpl(Provider<? extends Filter> filterProvider, FilterChain next) {
        this.filterProvider = filterProvider;
        this.next = next;
    }

    @Override
    public void next(Context context) {
        filterProvider.get().filter(next,context);
    }

    @Override
    public void end(Context context,Result result){
        writeResult(context.getRoutingContext().response(),result);
    }
}
