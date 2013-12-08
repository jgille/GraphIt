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

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;

public abstract class NumberComparatorTest {

    protected NumberComparator comparator;

    @Test
    public void testCompareToShort() {
        assertThat(comparator.compareTo(new Short((short) 1)), CoreMatchers.is(1));
        assertThat(comparator.compareTo(new Short((short) 10)), CoreMatchers.is(0));
        assertThat(comparator.compareTo(new Short((short) 20)), CoreMatchers.is(-1));
    }

    @Test
    public void testCompareToInt() {
        assertThat(comparator.compareTo(new Integer(1)), CoreMatchers.is(1));
        assertThat(comparator.compareTo(new Integer(10)), CoreMatchers.is(0));
        assertThat(comparator.compareTo(new Integer(20)), CoreMatchers.is(-1));
    }

    @Test
     public void testCompareToLong() {
        assertThat(comparator.compareTo(new Long(1)), CoreMatchers.is(1));
        assertThat(comparator.compareTo(new Long(10)), CoreMatchers.is(0));
        assertThat(comparator.compareTo(new Long(20)), CoreMatchers.is(-1));
    }

    @Test
     public void testCompareToDouble() {
        assertThat(comparator.compareTo(new Double(1)), CoreMatchers.is(1));
        assertThat(comparator.compareTo(new Double(10)), CoreMatchers.is(0));
        assertThat(comparator.compareTo(new Double(20)), CoreMatchers.is(-1));
    }

    @Test
     public void testCompareToFloat() {
        assertThat(comparator.compareTo(new Float(1)), CoreMatchers.is(1));
        assertThat(comparator.compareTo(new Float(10)), CoreMatchers.is(0));
        assertThat(comparator.compareTo(new Float(20)), CoreMatchers.is(-1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompareToNonNumeric() {
        comparator.compareTo("illegal");
    }
}
