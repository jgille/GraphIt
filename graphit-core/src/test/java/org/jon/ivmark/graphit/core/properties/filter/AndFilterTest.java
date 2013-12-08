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

import java.util.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AndFilterTest {

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyFilters() {
        new AndFilter(Collections.<Map<String, Object>>emptyList()).apply("test");
    }

    @Test
    public void testCompoundFilters() {
        List<Map<String, Object>> filters = new ArrayList<Map<String, Object>>();
        filters.add(Collections.<String, Object>singletonMap("exists", true));
        filters.add(Collections.<String, Object>singletonMap("eq", "test"));
        boolean valid = new AndFilter(filters).apply("test");
        assertTrue(valid);
    }

    @Test
    public void testNegativeCompoundFilters() {
        List<Map<String, Object>> filters = new ArrayList<Map<String, Object>>();
        filters.add(Collections.<String, Object>singletonMap("exists", true));
        filters.add(Collections.<String, Object>singletonMap("eq", "other"));
        boolean valid = new AndFilter(filters).apply("test");
        assertFalse(valid);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTooManyConditions() {
        List<Map<String, Object>> filters = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("1", 1);
        map.put("2", 2);
        filters.add(map);
        new AndFilter(filters);
    }

}
