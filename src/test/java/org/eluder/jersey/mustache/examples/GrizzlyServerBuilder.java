package org.eluder.jersey.mustache.examples;

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

import java.io.File;
import java.net.URI;
import java.util.EnumSet;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.Servlet;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.servlet.DefaultServlet;
import org.glassfish.grizzly.servlet.FilterRegistration;
import org.glassfish.grizzly.servlet.ServletRegistration;
import org.glassfish.grizzly.servlet.WebappContext;
import org.glassfish.grizzly.utils.ArraySet;
import org.glassfish.jersey.grizzly2.servlet.GrizzlyWebContainerFactory;

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
        HttpServer server = GrizzlyWebContainerFactory.create(URI.create(url));
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
        server.shutdownNow();
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
