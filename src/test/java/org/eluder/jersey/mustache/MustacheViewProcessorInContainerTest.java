package org.eluder.jersey.mustache;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;

public class MustacheViewProcessorInContainerTest extends JerseyTest {
    
    public MustacheViewProcessorInContainerTest() {
        super(new WebAppDescriptor.Builder()
                .initParam(PackagesResourceConfig.PROPERTY_PACKAGES, "org.eluder.jersey.mustache")
                .initParam(MustacheViewProcessor.MUSTACHE_TEMPLATE_EXPIRY, "2000")
                .build());
    }
    
    @Test
    public void testRenderTemplate() {
        String response = resource().path("mustache").get(String.class);
        assertEquals("this is bar bar", response);
    }
}
