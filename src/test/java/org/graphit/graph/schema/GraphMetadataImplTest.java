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

package org.graphit.graph.schema;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.graphit.graph.edge.schema.EdgeTypeImpl;
import org.graphit.graph.edge.schema.EdgeTypes;
import org.graphit.graph.node.schema.NodeType;
import org.graphit.graph.node.schema.NodeTypeImpl;
import org.graphit.graph.node.schema.NodeTypes;
import org.junit.Test;

/**
 * @author jon
 *
 */
public class GraphMetadataImplTest {

    @Test
    public void testGetGraphName() {
        GraphMetadataImpl metadata = new GraphMetadataImpl("foo");
        assertEquals("foo", metadata.getGraphName());
    }

    @Test
    public void testAddEdgeTypes() {
        GraphMetadataImpl metadata = new GraphMetadataImpl("foo");
        metadata.addEdgeType("A").addEdgeType(new EdgeTypeImpl("B")).addEdgeType("C", true);
        EdgeTypes edgeTypes = metadata.getEdgeTypes();
        assertNotNull(edgeTypes);
        assertEquals(3, edgeTypes.size());
        assertEquals(new EdgeTypeImpl("A"), edgeTypes.valueOf("A"));
        assertEquals(new EdgeTypeImpl("B"), edgeTypes.valueOf("B"));
        assertEquals(new EdgeTypeImpl("C", true), edgeTypes.valueOf("C"));
    }

    @Test
    public void testAddNodeTypes() {
        GraphMetadataImpl metadata = new GraphMetadataImpl("foo");
        metadata.addNodeType("A").addNodeType(new NodeTypeImpl("B"));
        NodeTypes nodeTypes = metadata.getNodeTypes();
        assertNotNull(nodeTypes);
        assertEquals(2, nodeTypes.size());
        List<NodeType> nodeTypeList = new ArrayList<NodeType>(nodeTypes.elements());
        assertEquals(Arrays.asList(new NodeTypeImpl("A"), new NodeTypeImpl("B")),
                     nodeTypeList);

    }
}
