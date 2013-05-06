package org.eluder.jersey.mustache.examples.guice;

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
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

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
            filter("/*").through(GuiceContainer.class);
            
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
