package org.eluder.jersey.mustache;

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
