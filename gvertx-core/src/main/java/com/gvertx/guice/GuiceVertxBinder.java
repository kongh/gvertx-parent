/*
 * The MIT License (MIT)
 * Copyright © 2016 Englishtown <opensource@englishtown.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the “Software”), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.gvertx.guice;

import com.google.inject.AbstractModule;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.core.eventbus.EventBus;

/**
 * Guice {@link AbstractModule} for vertx and container injections
 */
public class GuiceVertxBinder extends AbstractModule {

    private final Vertx vertx;

    private final io.vertx.core.Vertx coreVertx;

    public GuiceVertxBinder(Vertx vertx, io.vertx.core.Vertx coreVertx) {
        this.vertx = vertx;
        this.coreVertx = coreVertx;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void configure() {
        bind(Vertx.class).toInstance(vertx);
        bind(io.vertx.core.Vertx.class).toInstance(coreVertx);
        bind(EventBus.class).toInstance(vertx.eventBus());
        bind(io.vertx.core.eventbus.EventBus.class).toInstance(coreVertx.eventBus());
    }
}
