package com.gvertx.web;

import com.google.inject.AbstractModule;
import io.vertx.rxjava.ext.web.templ.ThymeleafTemplateEngine;

/**
 * Created by wangziqing on 17/3/13.
 */
public class RestModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(RouterHelp.class).to(RouterHelpImpl.class);
        bind(ThymeleafTemplateEngine.class).toInstance(ThymeleafTemplateEngine.create());
    }
}
