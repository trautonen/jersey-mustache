package org.eluder.jersey.mustache.util;

import com.sun.jersey.api.core.ResourceConfig;

public final class ResourceConfigHelper {

    private final ResourceConfig config;
    
    public ResourceConfigHelper(final ResourceConfig config) {
        this.config = config;
    }

    public String getStringProperty(final String name) {
        Object value = config.getProperty(name);
        if (value == null) {
            return null;
        }
        return value.toString();
    }
    
    public Integer getIntegerProperty(final String name) {
        Object value = config.getProperty(name);
        if (value == null) {
            return null;
        }
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return Integer.valueOf(value.toString());
    }
    
}
