package org.eluder.jersey.mustache.examples;

import java.io.File;
import java.net.URI;
import java.util.EnumSet;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.Servlet;

import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.servlet.DefaultServlet;
import org.glassfish.grizzly.servlet.FilterRegistration;
import org.glassfish.grizzly.servlet.ServletRegistration;
import org.glassfish.grizzly.servlet.WebappContext;
import org.glassfish.grizzly.utils.ArraySet;

import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory;

public class GrizzlyServerBuilder {

    private String url = "http://localhost:8989";
    private String context = "";
    private String mapping = "/*";
    private Class<? extends Filter> filter;
    private Class<? extends Servlet> servlet;
    private Class<? extends EventListener> listener;
    private final Map<String, String> initParameters = new HashMap<String, String>();
    
    public GrizzlyServerBuilder url(final String url) {
        this.url = url;
        return this;
    }
    
    public GrizzlyServerBuilder context(final String context) {
        this.context = context;
        return this;
    }
    
    public GrizzlyServerBuilder mapping(final String mapping) {
        this.mapping = mapping;
        return this;
    }
    
    public GrizzlyServerBuilder filter(final Class<? extends Filter> filter) {
        this.filter = filter;
        return this;
    }
    
    public GrizzlyServerBuilder servlet(final Class<? extends Servlet> servlet) {
        this.servlet = servlet;
        return this;
    }
    
    public GrizzlyServerBuilder listener(final Class<? extends EventListener> listener) {
        this.listener = listener;
        return this;
    }
    
    public GrizzlyServerBuilder addInitParameter(final String name, final String value) {
        this.initParameters.put(name, value);
        return this;
    }
    
    public GrizzlyServerBuilder addInitParameters(final Map<String, String> initParameters) {
        this.initParameters.putAll(initParameters);
        return this;
    }
    
    public void start() throws Exception {
        HttpServer server = GrizzlyServerFactory.createHttpServer(URI.create(url), (HttpHandler) null);
        WebappContext webapp = new WebappContext("GrizzlyContext", context);
        if (servlet == null) {
            servlet = DummyServlet.class;
        }
        if (servlet != null) {
            registerServlet(webapp);
        }
        if (filter != null) {
            registerFilter(webapp);
        }
        if (listener != null) {
            registerListener(webapp);
        }
        webapp.deploy(server);
        
        System.out.println(String.format("Jersey application started at %s\nHit enter to stop it...", url));
        System.in.read();
        server.stop();
    }

    private void registerServlet(final WebappContext webapp) {
        ServletRegistration registration = webapp.addServlet(servlet.getSimpleName(), servlet);
        registration.addMapping(mapping);
        registration.setInitParameters(initParameters);
        registration.setLoadOnStartup(1);
    }
    
    private void registerFilter(final WebappContext webapp) {
        FilterRegistration registration = webapp.addFilter(filter.getSimpleName(), filter);
        registration.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST, DispatcherType.INCLUDE, DispatcherType.FORWARD, DispatcherType.ERROR), mapping);
        registration.setInitParameters(initParameters);
    }
    
    private void registerListener(final WebappContext webapp) {
        webapp.addListener(listener);
    }
    
    public static class DummyServlet extends DefaultServlet {
        public DummyServlet() {
            super(new ArraySet<File>(File.class));
        }
    }
}
