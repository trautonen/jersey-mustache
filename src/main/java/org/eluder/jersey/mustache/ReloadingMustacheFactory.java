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

import java.io.File;
import java.util.concurrent.TimeUnit;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * Extension for default mustache factory to allow template reloading. This is extremely usefull
 * in development environment, where you can change the template when the application server is
 * running and it will be automatically refreshed. {@link #builder()} provides builder that can be
 * used to set the factory parameters dynamically.
 */
public class ReloadingMustacheFactory extends DefaultMustacheFactory {
    
    /** Value for disabling template reloading. */
    private static final int ETERNAL_CACHE_VALUE = -1;
    
    /** Default template expiration time in milliseconds. */
    private static final int DEFAULT_CACHE_EXPIRY = 3000;
    
    /**
     * Creates a new reloading mustache factory with the default values.
     */
    public ReloadingMustacheFactory() {
        super();
    }

    /**
     * Creates a new reloading mustache factory with the given file system root.
     * 
     * @param fileRoot the root folder for templates in file system
     */
    public ReloadingMustacheFactory(final File fileRoot) {
        super(fileRoot);
    }

    /**
     * Creates a new reloading mustache factory with the given resource root. Should not contain
     * leading slash.
     * 
     * @param resourceRoot the root folder for templates in classpath
     */
    public ReloadingMustacheFactory(final String resourceRoot) {
        super(resourceRoot);
    }
    
    @Override
    protected LoadingCache<String, Mustache> createMustacheCache() {
        int templateExpiryMillis = getTemplateExpiryMillis();
        CacheBuilder<Object, Object> builder = CacheBuilder.newBuilder();
        if (templateExpiryMillis > ETERNAL_CACHE_VALUE) {
            builder.expireAfterWrite(templateExpiryMillis, TimeUnit.MILLISECONDS);
        }
        return builder.build(createMustacheCacheLoader());
    }

    /**
     * @return the cache loader for mustache templates
     */
    protected CacheLoader<String, Mustache> createMustacheCacheLoader() {
        return new CacheLoader<String, Mustache>() {
            @Override
            public Mustache load(final String key) throws Exception {
                return mc.compile(key);
            }
        };
    }
    
    /**
     * @return the template expiration time in milliseconds
     */
    protected int getTemplateExpiryMillis() {
        return DEFAULT_CACHE_EXPIRY;
    }
    
    /**
     * Helper method to create factory builder.
     * 
     * @return the factory builder
     */
    public static Builder builder() {
        return new Builder();
    }
    
    /**
     * Builder that can be used to dynamically set the template expiration time.
     */
    public static class Builder {
        private File fileRoot;
        private String resourceRoot;
        private int templateExpiryMillis = DEFAULT_CACHE_EXPIRY;
        
        /**
         * Sets the file system root.
         * 
         * @param fileRoot the root folder for templates in file system
         * @return this for method chaining
         */
        public Builder fileRoot(final File fileRoot) {
            this.fileRoot = fileRoot;
            return this;
        }
        
        /**
         * Sets the resource root. Should not contain leading slash.
         * 
         * @param resourceRoot the root folder for templates in classpath
         * @return this for method chaining
         */
        public Builder resourceRoot(final String resourceRoot) {
            this.resourceRoot = resourceRoot;
            return this;
        }
        
        /**
         * Sets the template expiration time.
         * 
         * @param templateExpiryMillis the template expiration time in milliseconds
         * @return this for method chaining
         */
        public Builder templateExpiryMillis(final int templateExpiryMillis) {
            this.templateExpiryMillis = templateExpiryMillis;
            return this;
        }
        
        /**
         * @return the built reloading mustache factory
         */
        public ReloadingMustacheFactory build() {
            if (resourceRoot != null) {
                return new ReloadingMustacheFactory(resourceRoot) {
                    @Override
                    protected int getTemplateExpiryMillis() {
                        return templateExpiryMillis;
                    }
                };
            }
            
            if (fileRoot != null) {
                return new ReloadingMustacheFactory(fileRoot) {
                    @Override
                    protected int getTemplateExpiryMillis() {
                        return templateExpiryMillis;
                    }
                };
            }
            
            return new ReloadingMustacheFactory() {
                @Override
                protected int getTemplateExpiryMillis() {
                    return templateExpiryMillis;
                }
            };
        }
    }
}
