package org.eluder.jersey.mustache;

import java.io.File;
import java.util.concurrent.TimeUnit;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class ReloadingMustacheFactory extends DefaultMustacheFactory {
    
    private static final int ETERNAL_CACHE_VALUE = -1;
    private static final int DEFAULT_CACHE_EXPIRY = 3000;
    
    public ReloadingMustacheFactory() {
        super();
    }

    public ReloadingMustacheFactory(final File fileRoot) {
        super(fileRoot);
    }

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

    protected CacheLoader<String, Mustache> createMustacheCacheLoader() {
        return new CacheLoader<String, Mustache>() {
            @Override
            public Mustache load(final String key) throws Exception {
                return mc.compile(key);
            }
        };
    }
    
    protected int getTemplateExpiryMillis() {
        return DEFAULT_CACHE_EXPIRY;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private File fileRoot;
        private String resourceRoot;
        private int templateExpiryMillis = DEFAULT_CACHE_EXPIRY;
        
        public Builder fileRoot(final File fileRoot) {
            this.fileRoot = fileRoot;
            return this;
        }
        
        public Builder resourceRoot(final String resourceRoot) {
            this.resourceRoot = resourceRoot;
            return this;
        }
        
        public Builder templateExpiryMillis(final int templateExpiryMillis) {
            this.templateExpiryMillis = templateExpiryMillis;
            return this;
        }
        
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
