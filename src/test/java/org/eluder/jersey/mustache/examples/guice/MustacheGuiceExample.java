package org.eluder.jersey.mustache.examples.guice;

/*
 * #[license]
 * jersey-mustache
 * %%
 * Copyright (C) 2013 Tapio Rautonen
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * %[license]
 */

import java.util.concurrent.Executors;

import org.eluder.jersey.mustache.MustacheViewProcessor;
import org.eluder.jersey.mustache.ReloadingMustacheFactory;
import org.eluder.jersey.mustache.examples.GrizzlyServerBuilder;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.servlet.GuiceFilter;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;

public class MustacheGuiceExample {

    private static final String URL = "http://localhost:8989";
    
    public static void main(final String[] args) throws Exception {
        new GrizzlyServerBuilder()
            .url(URL)
            .listener(ServletListener.class)
            .filter(GuiceFilter.class)
            .start();
    }
    
    public static class ServletListener extends GuiceServletContextListener {
        @Override
        protected Injector getInjector() {
            return Guice.createInjector(new MustacheModule());
        }
    }
    
    public static class MustacheModule extends ServletModule {
        
        @Override
        protected void configureServlets() {
            //filter("/*").through(GuiceContainer.class);
            
            bind(GuiceResource.class).in(Singleton.class);
        }
        
        @Provides
        @Singleton
        public MustacheViewProcessor mustacheViewProcessor() {
            ReloadingMustacheFactory mustacheFactory = ReloadingMustacheFactory.builder()
                    .resourceRoot("examples")
                    .templateExpiryMillis(2000)
                    .build();
            mustacheFactory.setExecutorService(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()));
            return new MustacheViewProcessor(mustacheFactory);
        }
    }
}
