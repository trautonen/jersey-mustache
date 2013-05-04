package org.eluder.jersey.mustache.util;

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
