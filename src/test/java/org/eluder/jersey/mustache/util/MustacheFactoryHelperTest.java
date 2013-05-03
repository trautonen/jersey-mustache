package org.eluder.jersey.mustache.util;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.eluder.jersey.mustache.ReloadingMustacheFactory;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.MustacheFactory;
import com.sun.jersey.api.container.ContainerException;

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
