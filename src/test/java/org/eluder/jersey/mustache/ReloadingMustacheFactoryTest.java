package org.eluder.jersey.mustache;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.github.mustachejava.Mustache;

public class ReloadingMustacheFactoryTest {

    private final String template1 = "{{value}} bar";
    private final String template2 = "bar {{value}}";
    
    @Rule
    public final TemporaryFolder folder = new TemporaryFolder();

    private File file;

    @Before
    public void init() throws IOException {
        file = folder.newFile("template");
    }

    @Test
    public void testReloadingCache() throws Exception {
        ReloadingMustacheFactory mustacheFactory = createMustacheFactory(0);

        writeTemplate("template", template1);

        Mustache mustache1 = mustacheFactory.compile("template");
        assertEquals("foo bar", render(mustache1, new Context()));
        
        writeTemplate("template", template2);
        
        Mustache mustache2 = mustacheFactory.compile("template");
        assertEquals("bar foo", render(mustache2, new Context()));
    }
    
    @Test
    public void testEternalCache() throws Exception {
        ReloadingMustacheFactory mustacheFactory = createMustacheFactory(-1);
        
        writeTemplate("template", template1);

        Mustache mustache1 = mustacheFactory.compile("template");
        assertEquals("foo bar", render(mustache1, new Context()));
        
        writeTemplate("template", template2);
        
        Mustache mustache2 = mustacheFactory.compile("template");
        assertEquals("foo bar", render(mustache2, new Context()));
    }

    private ReloadingMustacheFactory createMustacheFactory(final int templateExpiryMillis) {
        return ReloadingMustacheFactory.builder()
                .fileRoot(folder.getRoot())
                .templateExpiryMillis(templateExpiryMillis)
                .build();
    }
    
    private void writeTemplate(final String name, final String template) throws Exception {
        OutputStreamWriter stream = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
        try {
            stream.write(template);
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }
    
    private String render(final Mustache mustache, final Object scope) {
        StringWriter writer = new StringWriter();
        mustache.execute(writer, scope);
        return writer.toString();
    }

    public static class Context {
        public String value = "foo";
    }
}
