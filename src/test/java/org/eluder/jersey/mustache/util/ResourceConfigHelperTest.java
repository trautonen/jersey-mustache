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
