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

import org.graphit.graph.edge.schema.DynamicEdgeTypes;
import org.graphit.graph.edge.schema.EdgeType;
import org.graphit.graph.edge.schema.EdgeTypes;
import org.graphit.graph.node.schema.DynamicNodeTypes;
import org.graphit.graph.node.schema.NodeType;
import org.graphit.graph.node.schema.NodeTypes;

/**
 *
 * A {@link GraphMetadata} instance where you can dynamically add node and edge
 * types.
 *
 * @author jon
 *
 */
public class DynamicGraphMetadata implements GraphMetadata {

    private final String graphName;
    private final DynamicNodeTypes nodeTypes;
    private final DynamicEdgeTypes edgeTypes;

    /**
     * Creates a new instance.
     */
    public DynamicGraphMetadata(String graphName) {
        this.graphName = graphName;
        this.nodeTypes = new DynamicNodeTypes();
        this.edgeTypes = new DynamicEdgeTypes();
    }

    @Override
    public String getGraphName() {
        return graphName;
    }

    @Override
    public NodeTypes getNodeTypes() {
        return nodeTypes;
    }

    @Override
    public EdgeTypes getEdgeTypes() {
        return edgeTypes;
    }

    /**
     * Adds a node type to the metadata.
     */
    public DynamicGraphMetadata addNodeType(NodeType nodeType) {
        nodeTypes.add(nodeType);
        return this;
    }

    /**
     * Adds a node type to the metadata.
     */
    public DynamicGraphMetadata addNodeType(String nodeTypeName) {
        nodeTypes.add(nodeTypeName);
        return this;
    }

    /**
     * Adds a edge type to the metadata.
     */
    public DynamicGraphMetadata addEdgeType(EdgeType edgeType) {
        edgeTypes.add(edgeType);
        return this;
    }

    /**
     * Adds a unweighted edge type to the metadata.
     */
    public DynamicGraphMetadata addEdgeType(String edgeTypeName) {
        edgeTypes.add(edgeTypeName);
        return this;
    }

    /**
     * Adds a edge type to the metadata.
     */
    public DynamicGraphMetadata addEdgeType(String edgeTypeName, boolean weighted) {
        edgeTypes.add(edgeTypeName, weighted);
        return this;
    }
}
