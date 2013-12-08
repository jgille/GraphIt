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

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LessThanOrEqualFilterTest {

    private NumberFilter filter;

    @Before
    public void init() {
        this.filter = new LessThanOrEqualFilter(10, new NumberComparatorFactory());
    }

    @Test
    public void testGreater() {
        assertFalse(filter.apply(20));
    }

    @Test
    public void testLesser() {
        assertTrue(filter.apply(1));
    }

    @Test
    public void testSame() {
        assertTrue(filter.apply(10));
    }
}
