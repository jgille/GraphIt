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

package org.graphit.graph.edge.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;

import org.graphit.graph.edge.util.TestEdgeType;
import org.graphit.graph.node.domain.Node;
import org.graphit.graph.node.domain.NodeId;
import org.graphit.graph.node.domain.NodeImpl;
import org.graphit.graph.node.domain.TestNodeType;
import org.graphit.properties.domain.HashMapProperties;
import org.graphit.properties.domain.Properties;
import org.junit.Test;

/**
 * @author jon
 *
 */
public class EdgeImplTest {

    @Test
    public void testGettersAndSetters() {
        Properties props = new HashMapProperties();
        props.setProperty("A", "B");
        EdgeImpl edge = new EdgeImpl(0, TestEdgeType.BOUGHT, props);
        assertEquals(0, edge.getIndex());
        assertEquals(TestEdgeType.BOUGHT, edge.getType());
        assertEquals(new EdgeId(TestEdgeType.BOUGHT, 0), edge.getEdgeId());
        assertNull(edge.getStartNode());
        assertNull(edge.getEndNode());
        assertEquals(0f, edge.getWeight(), 0.000001f);
        assertEquals("B", edge.getProperty("A"));
        assertNull(edge.getProperty("B"));

        assertEquals(new HashSet<String>(Arrays.asList("A")), edge.getPropertyKeys());

        edge.setProperty("B", "C");
        assertEquals("C", edge.getProperty("B"));

        Node startNode =
            new NodeImpl(0, new NodeId(TestNodeType.USER, "u1"), new HashMapProperties());
        edge.setStartNode(startNode);
        assertEquals(startNode, edge.getStartNode());

        Node endNode =
            new NodeImpl(0, new NodeId(TestNodeType.PRODUCT, "p1"), new HashMapProperties());
        edge.setEndNode(endNode);
        assertEquals(endNode, edge.getEndNode());

        edge.setWeight(1f);
        assertEquals(1f, edge.getWeight(), 0.000001f);
    }

    @Test
    public void testGetOppositeNode() {
        EdgeImpl edge = new EdgeImpl(0, TestEdgeType.BOUGHT, new HashMapProperties());
        Node startNode =
            new NodeImpl(0, new NodeId(TestNodeType.USER, "u1"), new HashMapProperties());
        edge.setStartNode(startNode);

        Node endNode =
            new NodeImpl(0, new NodeId(TestNodeType.PRODUCT, "p1"), new HashMapProperties());
        edge.setEndNode(endNode);

        assertEquals(startNode, edge.getOppositeNode(endNode.getNodeId()));
        assertEquals(endNode, edge.getOppositeNode(startNode.getNodeId()));

        boolean exception = false;
        try {
            edge.getOppositeNode(new NodeId(TestNodeType.PRODUCT, "p2"));
        } catch (IllegalArgumentException e) {
            exception = true;
        }
        assertTrue(exception);
    }

    @Test
    public void testEquals() {
        EdgeImpl edge1 = new EdgeImpl(0, TestEdgeType.BOUGHT, new HashMapProperties());
        EdgeImpl edge2 = new EdgeImpl(0, TestEdgeType.BOUGHT, new HashMapProperties());
        EdgeImpl edge3 = new EdgeImpl(1, TestEdgeType.BOUGHT, new HashMapProperties());

        assertEquals(edge1, edge1);
        assertEquals(edge1, edge2);
        assertFalse(edge1.equals(edge3));
        assertFalse(edge1.equals(null));
        assertFalse(edge1.equals(""));
    }
}
