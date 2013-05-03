package org.eluder.jersey.mustache.util;

import java.io.File;

import org.eluder.jersey.mustache.ReloadingMustacheFactory;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.MustacheFactory;
import com.sun.jersey.api.container.ContainerException;

public final class MustacheFactoryHelper {

    private final String factoryClass;
    private final String resourceRoot;
    private final File fileRoot;
    private final Integer templateExpiry;
    
    public MustacheFactoryHelper(final String factoryClass, final String resourceRoot, final String fileRoot, final Integer templateExpiry) {
        this.factoryClass = factoryClass;
        this.resourceRoot = resourceRoot;
        this.fileRoot = (fileRoot != null ? new File(fileRoot) : null);
        this.templateExpiry = templateExpiry;
    }
    
    public MustacheFactory createMustacheFactory() {
        if (templateExpiry != null) {
            return createReloadingMustacheFactory();
        } else {
            return createMustacheFactory(getFactoryClass());
        }
    }

    private Class<? extends MustacheFactory> getFactoryClass() {
        if (factoryClass == null) {
            return DefaultMustacheFactory.class;
        } else {
            try {
                return Class.forName(factoryClass).asSubclass(MustacheFactory.class);
            } catch (ClassNotFoundException ex) {
                throw new ContainerException("Mustache factory not found", ex);
            }
        }
    }
    
    private MustacheFactory createMustacheFactory(final Class<? extends MustacheFactory> clazz) {
        try {
            if (resourceRoot != null) {
                return clazz.getConstructor(String.class).newInstance(resourceRoot);
            }
            if (fileRoot != null) {
                return clazz.getConstructor(File.class).newInstance(fileRoot);
            }
            return clazz.getConstructor().newInstance();
        } catch (Exception ex) {
            throw new ContainerException("Failed to create mustache factory", ex);
        }
    }
    
    private MustacheFactory createReloadingMustacheFactory() {
        return ReloadingMustacheFactory.builder()
            .resourceRoot(resourceRoot)
            .fileRoot(fileRoot)
            .templateExpiryMillis(templateExpiry)
            .build();
    }
}
