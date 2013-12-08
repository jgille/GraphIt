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

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class NumberComparatorFactoryTest {

    @Test
    public void testShort() {
        short s = 10;
        NumberComparator comparator = new NumberComparatorFactory().comparatorFor(s);
        assertThat(comparator, notNullValue());
        assertThat("Unexpected class", comparator instanceof ShortComparator);
    }

    @Test
    public void testInt() {
        int i = 10;
        NumberComparator comparator = new NumberComparatorFactory().comparatorFor(i);
        assertThat(comparator, notNullValue());
        assertThat("Unexpected class", comparator instanceof IntComparator);
    }

    @Test
    public void testLong() {
        long l = 10;
        NumberComparator comparator = new NumberComparatorFactory().comparatorFor(l);
        assertThat(comparator, notNullValue());
        assertThat("Unexpected class", comparator instanceof LongComparator);
    }

    @Test
    public void testDouble() {
        double d = 10;
        NumberComparator comparator = new NumberComparatorFactory().comparatorFor(d);
        assertThat(comparator, notNullValue());
        assertThat("Unexpected class", comparator instanceof DoubleComparator);
    }

    @Test
    public void testFloat() {
        float f = 10;
        NumberComparator comparator = new NumberComparatorFactory().comparatorFor(f);
        assertThat(comparator, notNullValue());
        assertThat("Unexpected class", comparator instanceof FloatComparator);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegal() {
        new NumberComparatorFactory().comparatorFor("illegal");
    }
}
