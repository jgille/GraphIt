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

package org.graphit.graph.node.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodePrimitiveTest {

    @Test
    public void testValidateNullId() {
        NodePrimitive node = new NodePrimitive();
        node.setIndex(0);
        node.setType("foo");
        boolean exception = false;
        try {
            node.validate();
        } catch (IllegalArgumentException e) {
            exception = true;
        }
        assertTrue(exception);
    }

    @Test
    public void testValidateNullType() {
        NodePrimitive node = new NodePrimitive();
        node.setIndex(0);
        node.setId("foo");
        boolean exception = false;
        try {
            node.validate();
        } catch (IllegalArgumentException e) {
            exception = true;
        }
        assertTrue(exception);
    }

    @Test
    public void testValidateNoIndex() {
        NodePrimitive node = new NodePrimitive();
        node.setId("bar");
        node.setType("foo");
        boolean exception = false;
        try {
            node.validate();
        } catch (IllegalArgumentException e) {
            exception = true;
        }
        assertTrue(exception);
    }

    @Test
    public void testValidateValid() {
        NodePrimitive node = new NodePrimitive();
        node.setId("bar");
        node.setType("foo");
        node.setIndex(0);
        boolean exception = false;
        try {
            node.validate();
        } catch (IllegalArgumentException e) {
            exception = true;
        }
        assertFalse(exception);
    }

    @Test
    public void testGetters() {
        NodePrimitive node = new NodePrimitive(1, new NodeId(TestNodeType.PRODUCT, "foo"));
        assertEquals(1, node.getIndex());
        assertEquals("foo", node.getId());
        assertEquals(TestNodeType.PRODUCT.name(), node.getType());
    }

    @Test
    public void testEquals() {
        NodePrimitive n1 = new NodePrimitive(1, new NodeId(TestNodeType.PRODUCT, "foo"));
        NodePrimitive n2 = new NodePrimitive(1, new NodeId(TestNodeType.PRODUCT, "foo"));
        NodePrimitive n3 = new NodePrimitive(1, new NodeId(TestNodeType.PRODUCT, "bar"));
        NodePrimitive n4 = new NodePrimitive(1, new NodeId(TestNodeType.USER, "foo"));
        NodePrimitive n5 = new NodePrimitive(2, new NodeId(TestNodeType.PRODUCT, "foo"));
        NodePrimitive n6 = null;

        assertEquals(n1, n1);
        assertEquals(n1, n2);
        assertFalse(n1.equals(n3));
        assertFalse(n1.equals(n4));
        assertFalse(n1.equals(n5));
        assertFalse(n1.equals(n6));
        assertFalse(n1.equals(""));
    }

    @Test
    public void testHashCode() {
        NodePrimitive n1 = new NodePrimitive(1, new NodeId(TestNodeType.PRODUCT, "foo"));
        NodePrimitive n2 = new NodePrimitive(1, new NodeId(TestNodeType.PRODUCT, "foo"));
        NodePrimitive n3 = new NodePrimitive(1, new NodeId(TestNodeType.PRODUCT, "bar"));

        assertTrue(n1.hashCode() == n2.hashCode());
        assertFalse(n1.hashCode() == n3.hashCode());
    }
}
