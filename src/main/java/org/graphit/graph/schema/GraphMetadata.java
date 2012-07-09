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

/**
 * Describes a graph.
 *
 * @author jon
 *
 */
public interface GraphMetadata {

    /**
     * Gets the name of this graph.
     */
    String getGraphName();

    /**
     * Gets the valid node types for this graph.
     */
    NodeTypes getNodeTypes();

    /**
     * Gets the valid edge types for this graph.
     */
    EdgeTypes getEdgeTypes();

    /**
     * Ensures the existance of the given node type, creating it if absent.
     */
    NodeType getOrCreateNodeType(String nodeTypeName);

    /**
     * Ensures the existance of the given edge type, creating it if absent.
     */
    EdgeType getOrCreateEdgeType(String edgeTypeName);

    /**
     * Adds a node type to the metadata.
     */
    GraphMetadata addNodeType(NodeType nodeType);

    /**
     * Adds a node type to the metadata.
     */
    GraphMetadata addNodeType(String nodeTypeName);

    /**
     * Adds a edge type to the metadata.
     */
    GraphMetadata addEdgeType(EdgeType edgeType);

    /**
     * Adds a edge type to the metadata.
     */
    GraphMetadata addEdgeType(String edgeTypeName);
}
