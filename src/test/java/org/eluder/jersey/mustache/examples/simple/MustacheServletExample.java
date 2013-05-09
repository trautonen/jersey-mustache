package org.eluder.jersey.mustache.examples.simple;

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
