/*
 * Copyright 2012 Jon Ivmark
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jon.ivmark.graphit.core.properties.filter.it;

import org.jon.ivmark.graphit.core.Json;
import org.jon.ivmark.graphit.core.io.util.ResourceUtils;
import org.jon.ivmark.graphit.core.properties.HashMapProperties;
import org.jon.ivmark.graphit.core.properties.Properties;
import org.jon.ivmark.graphit.core.properties.filter.CompositePropertyFilter;
import org.jon.ivmark.graphit.core.properties.filter.PropertyFilterSettings;
import org.jon.ivmark.graphit.test.categories.IntegrationTest;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Category(IntegrationTest.class)
public class CompositePropertyFilterTestIt {

    @Test
    public void testAllowed() throws IOException {
        PropertyFilterSettings settings = loadSettings("fixtures/properties/PriceNameCategoryFilter.json");
        CompositePropertyFilter filter = new CompositePropertyFilter(settings);

        Properties properties = new PropertiesBuilder().withCategory("TestCategory").withName("Test")
                .withPrice(50).build();
        assertTrue(filter.apply(properties));
    }

    @Test
    public void testPriceNotWithinRange() throws IOException {
        PropertyFilterSettings settings = loadSettings("fixtures/properties/PriceNameCategoryFilter.json");
        CompositePropertyFilter filter = new CompositePropertyFilter(settings);

        Properties properties = new PropertiesBuilder().withCategory("TestCategory").withName("Test")
                .withPrice(1000).build();
        assertFalse(filter.apply(properties));
    }

    @Test
    public void testNameFilteredOut() throws IOException {
        PropertyFilterSettings settings = loadSettings("fixtures/properties/PriceNameCategoryFilter.json");
        CompositePropertyFilter filter = new CompositePropertyFilter(settings);

        Properties properties = new PropertiesBuilder().withCategory("TestCategory").withName("Unknown")
                .withPrice(50).build();
        assertFalse(filter.apply(properties));
    }

    @Test
    public void testCategoryFilteredOut() throws IOException {
        PropertyFilterSettings settings = loadSettings("fixtures/properties/PriceNameCategoryFilter.json");
        CompositePropertyFilter filter = new CompositePropertyFilter(settings);

        Properties properties = new PropertiesBuilder().withCategory("Unknown").withName("Test")
                .withPrice(50).build();
        assertFalse(filter.apply(properties));
    }

    private PropertyFilterSettings loadSettings(String path) throws IOException {
        File file = ResourceUtils.resourceFile(path);
        return Json.OBJECT_MAPPER.readValue(file, PropertyFilterSettings.class);
    }

    private static class PropertiesBuilder {

        private int price;
        private String name;
        private String category;

        private PropertiesBuilder withPrice(int price) {
            this.price = price;
            return this;
        }

        private PropertiesBuilder withName(String name) {
            this.name = name;
            return this;
        }

        private PropertiesBuilder withCategory(String category) {
            this.category = category;
            return this;
        }

        public Properties build() {
            Properties properties = new HashMapProperties();
            properties.setProperty("Price", price);
            properties.setProperty("Name", name);
            properties.setProperty("Category", category);
            return properties;
        }
    }

}
