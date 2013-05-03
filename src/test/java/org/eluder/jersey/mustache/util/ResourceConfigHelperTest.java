package org.eluder.jersey.mustache.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.sun.jersey.api.core.DefaultResourceConfig;

public class ResourceConfigHelperTest {

    private DefaultResourceConfig resourceConfig;
    private ResourceConfigHelper resourceConfigHelper;
    
    @Before
    public void init() {
        resourceConfig = new DefaultResourceConfig();
        resourceConfigHelper = new ResourceConfigHelper(resourceConfig);
    }
    
    @Test
    public void testGetMissingStringProperty() {
        assertNull(resourceConfigHelper.getStringProperty("foo"));
    }
    
    @Test
    public void testGetMissingIntegerProperty() {
        assertNull(resourceConfigHelper.getIntegerProperty("foo"));
    }
    
    @Test
    public void testGetStringProperty() {
        resourceConfig.setPropertiesAndFeatures(ImmutableMap.<String, Object>of("foo", "bar"));
        assertEquals("bar", resourceConfigHelper.getStringProperty("foo"));
    }
    
    @Test
    public void testGetIntegerAsStringProperty() {
        resourceConfig.setPropertiesAndFeatures(ImmutableMap.<String, Object>of("foo", 1));
        assertEquals("1", resourceConfigHelper.getStringProperty("foo"));
    }
    
    @Test
    public void testGetIntegerProperty() {
        resourceConfig.setPropertiesAndFeatures(ImmutableMap.<String, Object>of("foo", 1));
        assertEquals(Integer.valueOf(1), resourceConfigHelper.getIntegerProperty("foo"));
    }
    
    @Test
    public void testGetLongAsIntegerProperty() {
        resourceConfig.setPropertiesAndFeatures(ImmutableMap.<String, Object>of("foo", Long.valueOf(1)));
        assertEquals(Integer.valueOf(1), resourceConfigHelper.getIntegerProperty("foo"));
    }
    
    @Test
    public void testGetStringAsIntegerProperty() {
        resourceConfig.setPropertiesAndFeatures(ImmutableMap.<String, Object>of("foo", "1"));
        assertEquals(Integer.valueOf(1), resourceConfigHelper.getIntegerProperty("foo"));
    }
}
