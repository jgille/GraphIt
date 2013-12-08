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

import org.jon.ivmark.graphit.core.properties.HashMapProperties;
import org.jon.ivmark.graphit.core.properties.Properties;
import org.junit.Test;

import java.util.Collections;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class CompositePropertyFilterTest {

    @Test
    public void testWithSingleCondition() {
        Map<String, Object> testSettings = Collections.<String, Object>singletonMap("lt", 10);
        Map<String, Map<String, Object>> filterSettings = Collections.singletonMap("Test", testSettings);
        CompositePropertyFilter compositePropertyFilter = new CompositePropertyFilter(filterSettings);

        Properties properties = new HashMapProperties();
        properties.setProperty("Test", 20);

        assertThat(compositePropertyFilter.apply(properties), is(false));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithIllegalConditions() {
        Map<String, Object> testSettings = Collections.<String, Object>emptyMap();
        Map<String, Map<String, Object>> filterSettings = Collections.singletonMap("Test", testSettings);
        new CompositePropertyFilter(filterSettings);
    }

}
