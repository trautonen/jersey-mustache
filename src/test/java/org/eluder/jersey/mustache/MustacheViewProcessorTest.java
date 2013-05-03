package org.eluder.jersey.mustache;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;

import org.junit.Before;
import org.junit.Test;

import com.github.mustachejava.Mustache;
import com.sun.jersey.api.core.DefaultResourceConfig;
import com.sun.jersey.api.view.Viewable;

public class MustacheViewProcessorTest {
    
    private DefaultResourceConfig resourceConfig;
    private MustacheViewProcessor mustacheViewProcessor;
    
    @Before
    public void init() {
        resourceConfig = new DefaultResourceConfig();
        mustacheViewProcessor = new MustacheViewProcessor(resourceConfig);
    }
    
    @Test
    public void testResolveAndWriteTo() throws Exception {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Viewable viewable = new Viewable("template.mustache", new Context());
        Mustache mustache = mustacheViewProcessor.resolve(viewable.getTemplateName());
        mustacheViewProcessor.writeTo(mustache, viewable, stream);
        assertEquals("this is foo bar", stream.toString());
    }
    
    public static class Context {
        public String value = "foo";
    }
}
