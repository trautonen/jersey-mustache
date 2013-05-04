package org.eluder.jersey.mustache;

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

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

import org.eluder.jersey.mustache.util.MustacheFactoryHelper;
import org.eluder.jersey.mustache.util.ResourceConfigHelper;

import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.api.view.Viewable;
import com.sun.jersey.spi.resource.Singleton;
import com.sun.jersey.spi.template.ViewProcessor;

@Singleton
@Provider
public class MustacheViewProcessor implements ViewProcessor<Mustache> {
    
    public static final String MUSTACHE_FILE_ROOT       = "mustache.file.root";
    public static final String MUSTACHE_RESOURCE_ROOT   = "mustache.resource.root";
    public static final String MUSTACHE_FACTORY_CLASS   = "mustache.factory.class";
    public static final String MUSTACHE_TEMPLATE_EXPIRY = "mustache.template.expiry";
    
    private final MustacheFactory mustacheFactory;
    
    public MustacheViewProcessor(@Context final ResourceConfig resourceConfig) {
        ResourceConfigHelper config = new ResourceConfigHelper(resourceConfig);
        String factoryClass = config.getStringProperty(MUSTACHE_FACTORY_CLASS);
        String resourceRoot = config.getStringProperty(MUSTACHE_RESOURCE_ROOT);
        String fileRoot = config.getStringProperty(MUSTACHE_FILE_ROOT);
        Integer templateExpiry = config.getIntegerProperty(MUSTACHE_TEMPLATE_EXPIRY);
        
        MustacheFactoryHelper factory = new MustacheFactoryHelper(factoryClass, resourceRoot, fileRoot, templateExpiry);
        this.mustacheFactory = factory.createMustacheFactory();
    }
    
    @Override
    public Mustache resolve(final String name) {
        String relative = name;
        if (relative.startsWith("/")) {
            relative = relative.substring(1);
        }
        return compile(relative);
    }

    @Override
    public void writeTo(final Mustache t, final Viewable viewable, final OutputStream out) throws IOException {
        // send status and headers
        out.flush();
        
        // render the actual response
        OutputStreamWriter writer = new OutputStreamWriter(out);
        t.execute(writer, getScope(viewable)).flush();
    }

    protected Mustache compile(final String name) {
        return mustacheFactory.compile(name);
    }
    
    protected Object getScope(final Viewable viewable) {
        return viewable.getModel();
    }
}
