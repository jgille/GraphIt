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

package org.jon.ivmark.graphit.core.properties.filter;

import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class PropertyFilterSettingsTest {

    @Test
    public void testAppendFilter() {
        PropertyFilterSettings propertyFilterSettings = new PropertyFilterSettings().appendFilter("Price", ">", 10)
                .appendFilter("Price", "<", 20)
                .appendFilter("Name", "=", "Test");

        assertThat(propertyFilterSettings.size(), is(2));
        Map<String, Object> priceMap = new HashMap<String, Object>();
        priceMap.put(">", 10);
        priceMap.put("<", 20);
        assertThat(propertyFilterSettings.get("Price"), is(priceMap));

        Map<String, Object> nameMap = Collections.<String, Object>singletonMap("=", "Test");
        assertThat(propertyFilterSettings.get("Name"), is(nameMap));
    }
}
