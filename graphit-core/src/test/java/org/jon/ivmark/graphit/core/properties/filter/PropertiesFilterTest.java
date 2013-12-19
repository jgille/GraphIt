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

import java.util.List;

import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class PropertiesFilterTest {

    @Test
    public void testWithSingleCondition() {
        List<PropertyFilterSettings> filterSettings =
                singletonList(new PropertyFilterSettings("Test", singletonList(new FilterCondition("lt", 10))));
        PropertiesFilter compositePropertyFilter = new PropertiesFilter(filterSettings);

        Properties properties = new HashMapProperties();
        properties.setProperty("Test", 20);

        assertThat(compositePropertyFilter.apply(properties), is(false));
    }
}
