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

import org.jon.ivmark.graphit.core.properties.HashMapProperties;
import org.jon.ivmark.graphit.core.properties.Properties;
import org.junit.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

public class NodeImplTest {

    @Test
    public void testGetNodeId() {
        NodeId nodeId = new NodeId(TestNodeType.PRODUCT, "foo");
        Node node = new Node(0, nodeId, new HashMapProperties());
        assertEquals(nodeId, node.getNodeId());
    }

    @Test
    public void testGetNodeIndex() {
        NodeId nodeId = new NodeId(TestNodeType.PRODUCT, "foo");
        Node node = new Node(1, nodeId, new HashMapProperties());
        assertEquals(1, node.getIndex());
    }

    @Test
    public void testGetType() {
        NodeId nodeId = new NodeId(TestNodeType.PRODUCT, "foo");
        Node node = new Node(0, nodeId, new HashMapProperties());
        assertEquals(nodeId.getNodeType(), node.getType());
    }

    @Test
    public void testGetProperty() {
        NodeId nodeId = new NodeId(TestNodeType.PRODUCT, "foo");
        Properties properties = new HashMapProperties();
        properties.setProperty("A", "B");
        Node node = new Node(0, nodeId, properties);
        assertEquals("B", node.getProperty("A"));
        assertNull(node.getProperty("AB"));
    }

    @Test
    public void testRemoveProperty() {
        NodeId nodeId = new NodeId(TestNodeType.PRODUCT, "foo");
        Properties properties = new HashMapProperties();
        properties.setProperty("A", "B");
        Node node = new Node(0, nodeId, properties);
        assertEquals("B", node.removeProperty("A"));
        assertNull(node.getProperty("A"));
    }

    @Test
    public void testSetProperty() {
        NodeId nodeId = new NodeId(TestNodeType.PRODUCT, "foo");
        Properties properties = new HashMapProperties();
        properties.setProperty("A", "B");
        Node node = new Node(0, nodeId, properties);
        assertEquals("B", node.getProperty("A"));
    }

    @Test
    public void testContainsProperty() {
        NodeId nodeId = new NodeId(TestNodeType.PRODUCT, "foo");
        Properties properties = new HashMapProperties();
        properties.setProperty("A", "B");
        properties.setProperty("B", null);

        Node node = new Node(0, nodeId, properties);
        assertTrue(node.containsProperty("A"));
        assertTrue(node.containsProperty("B"));
        assertFalse(node.containsProperty("C"));
    }

    @Test
    public void testNullKey() {
        NodeId nodeId = new NodeId(TestNodeType.PRODUCT, "foo");
        Properties properties = new HashMapProperties();
        properties.setProperty(null, "B");
        Node node = new Node(0, nodeId, properties);
        assertEquals("B", node.getProperty(null));

        node.setProperty(null, "C");
        assertEquals("C", node.getProperty(null));

        assertTrue(node.containsProperty(null));
        assertEquals("C", node.removeProperty(null));
        assertFalse(node.containsProperty(null));
    }

    @Test
    public void testGetPropertyKeys() {
        NodeId nodeId = new NodeId(TestNodeType.PRODUCT, "foo");
        Properties properties = new HashMapProperties();
        properties.setProperty("A", "B");
        Node node = new Node(0, nodeId, properties);

        Set<String> keys = node.getPropertyKeys();
        assertEquals(1, keys.size());
        assertEquals("A", keys.iterator().next());
    }

    @Test
    public void testAsPropertyMap() {
        NodeId nodeId = new NodeId(TestNodeType.PRODUCT, "foo");
        Properties properties = new HashMapProperties();
        properties.setProperty("A", "B");
        Node node = new Node(0, nodeId, properties);

        Map<String, Object> map = node.asPropertyMap();
        assertEquals(1, map.size());
        Map.Entry<String, Object> entry = map.entrySet().iterator().next();
        assertEquals("A", entry.getKey());
        assertEquals("B", entry.getValue());
    }

    @Test
    public void testEquals() {
        Node n1 =
            new Node(1, new NodeId(TestNodeType.PRODUCT, "foo"), new HashMapProperties());
        Node n2 =
            new Node(1, new NodeId(TestNodeType.PRODUCT, "foo"), new HashMapProperties());
        Node n3 =
            new Node(2, new NodeId(TestNodeType.PRODUCT, "foo"), new HashMapProperties());
        Node n4 =
            new Node(1, new NodeId(TestNodeType.PRODUCT, "bar"), new HashMapProperties());
        Node n5 =
            new Node(1, new NodeId(TestNodeType.USER, "foo"), new HashMapProperties());
        Node n6 = null;
        Node n7 =
            new Node(1, null, new HashMapProperties());

        assertEquals(n1, n1);
        assertEquals(n1, n2);
        assertEquals(n1, n3);
        assertFalse(n1.equals(n4));
        assertFalse(n1.equals(n5));
        assertFalse(n1.equals(n6));
        assertFalse(n1.equals(n7));
        assertFalse(n7.equals(n1));
        assertEquals(n7, n7);
        assertFalse(n1.equals(""));
    }

    @Test
    public void testHashCode() {
        Node n1 =
            new Node(1, new NodeId(TestNodeType.PRODUCT, "foo"), new HashMapProperties());
        Node n2 =
            new Node(1, new NodeId(TestNodeType.PRODUCT, "foo"), new HashMapProperties());
        Node n3 =
            new Node(2, new NodeId(TestNodeType.PRODUCT, "foo"), new HashMapProperties());
        Node n4 =
            new Node(1, new NodeId(TestNodeType.PRODUCT, "bar"), new HashMapProperties());
        Node n5 = new Node(1, null, new HashMapProperties());

        assertTrue(n1.hashCode() == n2.hashCode());
        assertTrue(n1.hashCode() == n3.hashCode());
        assertFalse(n1.hashCode() == n4.hashCode());
        assertFalse(n1.hashCode() == n5.hashCode());
        assertTrue(n5.hashCode() == n5.hashCode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeIndex() {
        new Node(-1, new NodeId(TestNodeType.PRODUCT, "foo"), new HashMapProperties());
    }
}
