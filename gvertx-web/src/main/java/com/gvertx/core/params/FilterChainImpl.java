/**
 * Copyright (C) 2012-2017 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gvertx.core.params;

import com.google.inject.Provider;
import com.gvertx.core.utils.WriteHelp;
import com.gvertx.core.models.Context;
import com.gvertx.core.models.Result;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.ext.web.templ.ThymeleafTemplateEngine;

/**
 * Implementation of the filter chain
 */
public class FilterChainImpl implements FilterChain {

    private final Provider<? extends Filter> filterProvider;
    private final FilterChain next;
    private final Vertx vertx;
    private final ThymeleafTemplateEngine engine;

    public FilterChainImpl(Provider<? extends Filter> filterProvider, FilterChain next, Vertx vertx,ThymeleafTemplateEngine engine) {
        this.filterProvider = filterProvider;
        this.next = next;
        this.vertx = vertx;
        this.engine = engine;
    }

    @Override
    public void next(Context context) {
        filterProvider.get().filter(next, context);
    }

    @Override
    public void end(Context context, Result result) {
        WriteHelp.end(context.getRoutingContext(), result, vertx ,engine);
    }
}
