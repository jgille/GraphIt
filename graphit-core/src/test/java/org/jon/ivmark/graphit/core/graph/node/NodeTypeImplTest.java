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

package org.jon.ivmark.graphit.core.graph.node;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * @author jon
 * 
 */
public class NodeTypeImplTest {

    @Test
    public void testName() {
        NodeTypeImpl foo = new NodeTypeImpl("foo");
        assertEquals("foo", foo.name());
    }

    @Test
    public void testEquals() {
        NodeTypeImpl n1 = new NodeTypeImpl("foo");
        NodeTypeImpl n2 = new NodeTypeImpl("foo");
        NodeTypeImpl n3 = new NodeTypeImpl("bar");
        NodeTypeImpl n4 = new NodeTypeImpl(null);
        assertEquals(n1, n1);
        assertEquals(n1, n2);
        assertFalse(n1.equals(n3));
        assertFalse(n1.equals(n4));
        assertEquals(n4, n4);
        assertFalse(n4.equals(n1));
        assertFalse(n1.equals(null));
        assertFalse(n1.equals(""));
    }

    @Test
    public void testHashCode() {
        NodeTypeImpl n1 = new NodeTypeImpl("foo");
        NodeTypeImpl n2 = new NodeTypeImpl("foo");
        NodeTypeImpl n3 = new NodeTypeImpl("bar");
        NodeTypeImpl n4 = new NodeTypeImpl(null);
        assertEquals(n1.hashCode(), n1.hashCode());
        assertEquals(n1.hashCode(), n2.hashCode());
        assertFalse(n1.hashCode() == n3.hashCode());
        assertFalse(n1.hashCode() == n4.hashCode());
    }
}
