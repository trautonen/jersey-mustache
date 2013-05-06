package org.eluder.jersey.mustache.examples.simple;

import java.util.HashMap;
import java.util.Map;

import org.eluder.jersey.mustache.MustacheViewProcessor;
import org.eluder.jersey.mustache.examples.GrizzlyServerBuilder;

import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.spi.container.servlet.ServletContainer;

public class MustacheServletExample {

    private static final String URL = "http://localhost:8989";
    
    private static Map<String, String> initParams() {
        Map<String, String> initParams = new HashMap<String, String>();
        initParams.put(PackagesResourceConfig.PROPERTY_PACKAGES, "org.eluder.jersey.mustache");
        initParams.put(MustacheViewProcessor.MUSTACHE_RESOURCE_ROOT, "examples");
        initParams.put(MustacheViewProcessor.MUSTACHE_TEMPLATE_EXPIRY, "2000");
        return initParams;
    }
    
    public static void main(final String[] args) throws Exception {
        new GrizzlyServerBuilder()
            .url(URL)
            .servlet(ServletContainer.class)
            .addInitParameters(initParams())
            .start();
    }
    
}
