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

package org.jon.ivmark.graphit.core.graph;

import org.jon.ivmark.graphit.core.graph.edge.EdgeType;
import org.jon.ivmark.graphit.core.graph.edge.EdgeTypes;
import org.jon.ivmark.graphit.core.graph.node.NodeType;
import org.jon.ivmark.graphit.core.graph.node.NodeTypes;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author jon
 * 
 */
public class GraphMetadataTest {

    @Test
    public void testGetGraphName() {
        GraphMetadata metadata = new GraphMetadata("foo");
        assertEquals("foo", metadata.getGraphName());
    }

    @Test
    public void testAddEdgeTypes() {
        GraphMetadata metadata = new GraphMetadata("foo");
        metadata.addEdgeType("A").addEdgeType(new EdgeType("B")).addEdgeType("C");
        EdgeTypes edgeTypes = metadata.getEdgeTypes();
        assertNotNull(edgeTypes);
        assertEquals(3, edgeTypes.size());
        assertEquals(new EdgeType("A"), edgeTypes.valueOf("A"));
        assertEquals(new EdgeType("B"), edgeTypes.valueOf("B"));
        assertEquals(new EdgeType("C"), edgeTypes.valueOf("C"));
    }

    @Test
    public void testAddNodeTypes() {
        GraphMetadata metadata = new GraphMetadata("foo");
        metadata.addNodeType("A").addNodeType(new NodeType("B"));
        NodeTypes nodeTypes = metadata.getNodeTypes();
        assertNotNull(nodeTypes);
        assertEquals(2, nodeTypes.size());
        List<NodeType> nodeTypeList = new ArrayList<NodeType>(nodeTypes.elements());
        assertEquals(Arrays.<NodeType>asList(new NodeType("A"), new NodeType("B")),
                     nodeTypeList);

    }
}
