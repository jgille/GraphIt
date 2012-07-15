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
import org.graphit.graph.edge.schema.EdgeTypesImpl;
import org.graphit.graph.node.schema.NodeType;
import org.graphit.graph.node.schema.NodeTypes;
import org.graphit.graph.node.schema.NodeTypesImpl;
import org.springframework.util.Assert;

/**
 *
 * A {@link GraphMetadata} implementation.
 *
 * @author jon
 *
 */
public class GraphMetadataImpl implements GraphMetadata {

    private String graphName;
    private final NodeTypes nodeTypes;
    private final EdgeTypes edgeTypes;

    /**
     * Creates a new instance.
     */
    public GraphMetadataImpl() {
        this("");
    }

    /**
     * Creates a new instance.
     */
    public GraphMetadataImpl(String graphName) {
        this(graphName, new NodeTypesImpl(), new EdgeTypesImpl());
    }

    /**
     * Creates a new instance.
     */
    public GraphMetadataImpl(String graphName, NodeTypes nodeTypes, EdgeTypes edgeTypes) {
        Assert.notNull(graphName);
        this.graphName = graphName;
        this.nodeTypes = nodeTypes;
        this.edgeTypes = edgeTypes;

    }

    @Override
    public void setGraphName(String graphName) {
        Assert.isTrue(!graphName.isEmpty());
        this.graphName = graphName;
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

    @Override
    public NodeType getOrCreateNodeType(String nodeTypeName) {
        return nodeTypes.getOrAdd(nodeTypeName);
    }

    @Override
    public EdgeType getOrCreateEdgeType(String edgeTypeName) {
        return edgeTypes.getOrAdd(edgeTypeName);
    }

    @Override
    public GraphMetadataImpl addNodeType(NodeType nodeType) {
        nodeTypes.add(nodeType);
        return this;
    }

    @Override
    public GraphMetadataImpl addNodeType(String nodeTypeName) {
        nodeTypes.add(nodeTypeName);
        return this;
    }

    @Override
    public GraphMetadataImpl addEdgeType(EdgeType edgeType) {
        edgeTypes.add(edgeType);
        return this;
    }

    @Override
    public GraphMetadataImpl addEdgeType(String edgeTypeName) {
        edgeTypes.add(edgeTypeName);
        return this;
    }

    @Override
    public String toString() {
        return "GraphMetadataImpl [graphName=" + graphName + ", nodeTypes=" + nodeTypes
            + ", edgeTypes=" + edgeTypes + "]";
    }

}
