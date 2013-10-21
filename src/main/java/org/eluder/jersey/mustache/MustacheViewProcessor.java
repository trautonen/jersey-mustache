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

import javax.inject.Singleton;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import org.eluder.jersey.mustache.util.MustacheFactoryHelper;
import org.eluder.jersey.mustache.util.ResourceConfigHelper;
import org.glassfish.jersey.server.mvc.Viewable;
import org.glassfish.jersey.server.mvc.spi.TemplateProcessor;

import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

/**
 * View processor implementation to render mustache templates. The mustache factory can be
 * configured from the servlet or filter init parameters, or straight from the
 * {@link Configuration} or manually creating an instance of the processor supplying a mustache
 * factory instance.
 * <p>
 * Jersey will instantiate the view processor automatically if this class is available for the
 * resource config implementation. Otherwise the constructor with mustache factory argument can
 * be used to create the instance.
 * <p>
 * If there are no init properties set, the mustache factory will be the the default mustache
 * factory initiated with default parameters. Optionally file root or resource root can be set to
 * define where the templates are located. Setting custom factory class name will allow fine
 * grained tuning of the factory. Setting template expiry time provides automatic template
 * reloading. Note that setting the expiry parameter will always select
 * {@link ReloadingMustacheFactory} as the factory implementation and the factory class parameter
 * will be ignored. This is due to the poor initialization code of the mustache factory. More
 * customization can be achieved by extending the {@link ReloadingMustacheFactory} and providing
 * the extended implementation as factory class.
 */
@Singleton
@Provider
public class MustacheViewProcessor implements TemplateProcessor<Mustache> {
    
    /** Folder in file system for mustache templates. */
    public static final String MUSTACHE_FILE_ROOT       = "mustache.file.root";
    
    /** Resource folder in classpath for mustache templates. Should not contain leading slash. */
    public static final String MUSTACHE_RESOURCE_ROOT   = "mustache.resource.root";
    
    /** Fully qualified name of custom mustache factory implementation class. */
    public static final String MUSTACHE_FACTORY_CLASS   = "mustache.factory.class";
    
    /** Mustache template expiry in milliseconds. Setting this will always default to {@link ReloadingMustacheFactory}. */
    public static final String MUSTACHE_TEMPLATE_EXPIRY = "mustache.template.expiry";
    
    private final MustacheFactory mustacheFactory;
    
    /**
     * Creates a new mustache view processor from the init parameters defined in resource config.
     * 
     * @param configuration the configuration
     */
    public MustacheViewProcessor(@Context final Configuration configuration) {
        ResourceConfigHelper config = new ResourceConfigHelper(configuration);
        String factoryClass = config.getStringProperty(MUSTACHE_FACTORY_CLASS);
        String resourceRoot = config.getStringProperty(MUSTACHE_RESOURCE_ROOT);
        String fileRoot = config.getStringProperty(MUSTACHE_FILE_ROOT);
        Integer templateExpiry = config.getIntegerProperty(MUSTACHE_TEMPLATE_EXPIRY);
        
        MustacheFactoryHelper factory = new MustacheFactoryHelper(factoryClass, resourceRoot, fileRoot, templateExpiry);
        this.mustacheFactory = factory.createMustacheFactory();
    }
    
    /**
     * Creates a new mustache view processor from the given mustache factory.
     * 
     * @param mustacheFactory the mustache factory
     */
    public MustacheViewProcessor(final MustacheFactory mustacheFactory) {
        this.mustacheFactory = mustacheFactory;
    }
    
    @Override
    public Mustache resolve(final String name, final MediaType mediaType) {
        String relative = name;
        if (relative.charAt(0) == '/') {
            relative = relative.substring(1);
        }
        return compile(relative);
    }
    
    @Override
    public void writeTo(final Mustache templateReference, final Viewable viewable, final MediaType mediaType, final OutputStream out) throws IOException {
        // send status and headers
        out.flush();
        
        // render the actual template
        OutputStreamWriter writer = new OutputStreamWriter(out);
        templateReference.execute(writer, getScope(viewable)).close();
    }

    /**
     * Compile the mustache template with given name.
     * 
     * @param name the template name
     * @return compiled mustache template
     */
    protected Mustache compile(final String name) {
        return mustacheFactory.compile(name);
    }
    
    /**
     * Returns the scope object for the mustache template.
     * 
     * @param viewable the view to render
     * @return scope for the mustache template
     */
    protected Object getScope(final Viewable viewable) {
        return viewable.getModel();
    }
}
