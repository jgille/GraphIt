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

import org.graphit.graph.edge.schema.EdgeType;
import org.graphit.graph.edge.schema.EdgeTypes;
import org.graphit.graph.node.schema.NodeType;
import org.graphit.graph.node.schema.NodeTypes;
import org.springframework.util.Assert;

/**
 *
 * A {@link GraphMetadata} implementation.
 *
 * @author jon
 *
 */
public final class GraphMetadata {

    private String graphName;
    private final NodeTypes nodeTypes;
    private final EdgeTypes edgeTypes;

    /**
     * Creates a new instance.
     */
    public GraphMetadata() {
        this("");
    }

    /**
     * Creates a new instance.
     */
    public GraphMetadata(String graphName) {
        this(graphName, new NodeTypes(), new EdgeTypes());
    }

    /**
     * Creates a new instance.
     */
    private GraphMetadata(String graphName, NodeTypes nodeTypes, EdgeTypes edgeTypes) {
        Assert.notNull(graphName);
        this.graphName = graphName;
        this.nodeTypes = nodeTypes;
        this.edgeTypes = edgeTypes;

    }

    /**
     * Sets the name of the graph.
     */
    public void setGraphName(String graphName) {
        Assert.hasText(graphName, "The graph name must not be empty.");
        this.graphName = graphName;
    }

    /**
     * Gets the name of the graph.
     */
    public String getGraphName() {
        return graphName;
    }

    /**
     * Gets the valid node types for the graph.
     */
    public NodeTypes getNodeTypes() {
        return nodeTypes;
    }

    /**
     * Gets the valid edge types for the graph.
     */
    public EdgeTypes getEdgeTypes() {
        return edgeTypes;
    }

    /**
     * Ensures the existance of the given node type, creating it if absent.
     */
    public NodeType getOrCreateNodeType(String nodeTypeName) {
        return nodeTypes.getOrAdd(nodeTypeName);
    }

    /**
     * Ensures the existance of the given edge type, creating it if absent.
     */

    public EdgeType getOrCreateEdgeType(String edgeTypeName) {
        return edgeTypes.getOrAdd(edgeTypeName);
    }

    /**
     * Adds a node type.
     */
    public GraphMetadata addNodeType(NodeType nodeType) {
        nodeTypes.add(nodeType);
        return this;
    }

    /**
     * Adds a node type.
     */
    public GraphMetadata addNodeType(String nodeTypeName) {
        nodeTypes.add(nodeTypeName);
        return this;
    }

    /**
     * Adds an edgetype.
     */
    public GraphMetadata addEdgeType(EdgeType edgeType) {
        edgeTypes.add(edgeType);
        return this;
    }

    /**
     * Adds an edgetype.
     */
    public GraphMetadata addEdgeType(String edgeTypeName) {
        edgeTypes.add(edgeTypeName);
        return this;
    }

    @Override
    public String toString() {
        return "GraphMetadata [graphName=" + graphName + ", nodeTypes=" + nodeTypes
            + ", edgeTypes=" + edgeTypes + "]";
    }

}
