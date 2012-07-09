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

import org.graphit.graph.edge.schema.EdgeTypesImpl;
import org.graphit.graph.edge.schema.EdgeType;
import org.graphit.graph.edge.schema.EdgeTypes;
import org.graphit.graph.node.schema.NodeTypesImpl;
import org.graphit.graph.node.schema.NodeType;
import org.graphit.graph.node.schema.NodeTypes;

/**
 *
 * A {@link GraphMetadata} implementation.
 *
 * @author jon
 *
 */
public class GraphMetadataImpl implements GraphMetadata {

    private final String graphName;
    private final NodeTypesImpl nodeTypes;
    private final EdgeTypesImpl edgeTypes;

    /**
     * Creates a new instance.
     */
    public GraphMetadataImpl(String graphName) {
        this.graphName = graphName;
        this.nodeTypes = new NodeTypesImpl();
        this.edgeTypes = new EdgeTypesImpl();
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
        nodeTypes.ensureHasNodeType(nodeTypeName);
        return nodeTypes.valueOf(nodeTypeName);
    }

    @Override
    public EdgeType getOrCreateEdgeType(String edgeTypeName) {
        edgeTypes.ensureHasEdgeType(edgeTypeName);
        return edgeTypes.valueOf(edgeTypeName);
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
}
