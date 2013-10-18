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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.eluder.jersey.mustache.ReloadingMustacheFactory;
import org.glassfish.jersey.server.ContainerException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.MustacheFactory;

public class MustacheFactoryHelperTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    
    @Test
    public void testCreateReloadingMustacheFactory() {
        MustacheFactoryHelper helper = new MustacheFactoryHelper(null, null, folder.getRoot().getAbsolutePath(), 10);
        assertMustacheFactory(ReloadingMustacheFactory.class, helper.createMustacheFactory());
    }
    
    @Test
    public void testCreateDefaultMustacheFactory() {
        MustacheFactoryHelper helper = new MustacheFactoryHelper(null, null, null, null);
        assertMustacheFactory(DefaultMustacheFactory.class, helper.createMustacheFactory());
    }
    
    @Test
    public void testCreateDefaultMustacheFactoryFromFileRoot() {
        MustacheFactoryHelper helper = new MustacheFactoryHelper(null, null, folder.getRoot().getAbsolutePath(), null);
        assertMustacheFactory(DefaultMustacheFactory.class, helper.createMustacheFactory());
    }
    
    @Test
    public void testCreateDefaultMustacheFactoryFromResourceRoot() {
        MustacheFactoryHelper helper = new MustacheFactoryHelper(null, "", null, null);
        assertMustacheFactory(DefaultMustacheFactory.class, helper.createMustacheFactory());
    }
    
    @Test
    public void testCreateDefinedMustacheFactory() {
        MustacheFactoryHelper helper = new MustacheFactoryHelper(DefaultMustacheFactory.class.getName(), null, null, null);
        assertMustacheFactory(DefaultMustacheFactory.class, helper.createMustacheFactory());
    }
    
    @Test(expected = ContainerException.class)
    public void testCreateDefinedMustacheFactoryWithInvalidConstructor() {
        MustacheFactoryHelper helper = new MustacheFactoryHelper(CustomMustacheFactory.class.getName(), null, null, null);
        helper.createMustacheFactory();
    }
    
    @Test(expected = ContainerException.class)
    public void testCreateDefinedMustacheFactoryWithInvalidClass() {
        MustacheFactoryHelper helper = new MustacheFactoryHelper("this.factory.is.missing.Factory", null, null, null);
        helper.createMustacheFactory();
    }
    
    private static void assertMustacheFactory(final Class<? extends MustacheFactory> expectedType, final MustacheFactory instance) {
        assertNotNull(instance);
        assertTrue(expectedType.isInstance(instance));
    }
    
    public static class CustomMustacheFactory extends DefaultMustacheFactory {
        public CustomMustacheFactory(final String resourceRoot) {
            super(resourceRoot);
        }
    }
}
