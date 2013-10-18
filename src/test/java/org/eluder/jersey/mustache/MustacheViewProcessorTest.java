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

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;

import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.mvc.Viewable;
import org.junit.Before;
import org.junit.Test;

import com.github.mustachejava.Mustache;

public class MustacheViewProcessorTest {
    
    private ResourceConfig resourceConfig;
    private MustacheViewProcessor mustacheViewProcessor;
    
    @Before
    public void init() {
        resourceConfig = new ResourceConfig();
        mustacheViewProcessor = new MustacheViewProcessor(resourceConfig);
    }
    
    @Test
    public void testResolveAndWriteTo() throws Exception {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Viewable viewable = new Viewable("template.mustache", new Context());
        Mustache mustache = mustacheViewProcessor.resolve(viewable.getTemplateName(), MediaType.TEXT_HTML_TYPE);
        mustacheViewProcessor.writeTo(mustache, viewable, MediaType.TEXT_HTML_TYPE, stream);
        assertEquals("this is foo bar", stream.toString());
    }
    
    public static class Context {
        public String value = "foo";
    }
}
