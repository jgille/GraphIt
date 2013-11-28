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

package org.graphit.graph.service;

import java.io.File;

import org.graphit.common.collections.IterablePipe;
import org.graphit.graph.edge.domain.Edge;
import org.graphit.graph.edge.domain.EdgeId;
import org.graphit.graph.edge.schema.EdgeSortOrder;
import org.graphit.graph.edge.schema.EdgeType;
import org.graphit.graph.node.domain.Node;
import org.graphit.graph.node.domain.NodeId;
import org.graphit.graph.node.schema.NodeType;
import org.graphit.graph.schema.GraphMetadata;
import org.graphit.graph.traversal.EdgeDirection;
import org.graphit.properties.domain.Properties;

/**
 * A graph containing nodes connected by edges. Both nodes and edges can have
 * properties associated with them.
 *
 * @author jon
 *
 */
public interface PropertyGraph {

    /**
     * Initiates the graph.
     */
    void init();

    /**
     * Shuts the graph down.
     */
    void shutdown();

    /**
     * Gets metadata describing the valid nodes and edges for this graph.
     */
    GraphMetadata getMetadata();

    /**
     * Sets the name of this graph.
     */
    void setGraphName(String graphName);

    /**
     * Creates an edge type with the given name and a default sort order. Throws
     * an exception if such a type already exists.
     */
    EdgeType createEdgeType(String name);

    /**
     * Creates an edge type with the given name and sort order. Throws an
     * exception if such a type already exists.
     */
    EdgeType createEdgeType(String name, EdgeSortOrder sortOrder);

    /**
     * Creates a node type with the given name. Throws an exception if such a
     * type already exists.
     */
    NodeType createNodeType(String name);

    /**
     * Gets the edge type with the given name, throwing an exception if no such
     * edge type exists.
     */
    EdgeType getEdgeType(String name);

    /**
     * Gets the edge type with the given name, or creates it if none exists.
     */
    EdgeType getOrCreateEdgeType(String name);

    /**
     * Gets the node type with the given name, throwing an exception if no such
     * npde type exists.
     */
    NodeType getNodeType(String name);

    /**
     * Gets the node type with the given name, or creates it if none exists.
     */
    NodeType getOrCreateNodeType(String name);

    /**
     * Gets an iterable of edges connected to a node.
     */
    IterablePipe<Edge> getEdges(NodeId node, EdgeType edgeType, EdgeDirection direction);

    /**
     * Gets an iterable of neighbors for a node.
     */
    IterablePipe<Node> getNeighbors(NodeId node, EdgeType edgeType, EdgeDirection direction);

    /**
     * Gets a node by it's index. Modifying the returned node's properties will
     * take immediate effect in the backing repository.
     */
    Node getNode(int index);

    /**
     * Gets a node by it's id. Modifying the returned node's properties will
     * take immediate effect in the backing repository.
     */
    Node getNode(NodeId nodeId);

    /**
     * Adds a node. Modifying the returned node's properties will take immediate
     * effect in the backing repository
     */
    Node addNode(NodeId nodeId);

    /**
     * Adds a node. You should normally not use this method, use
     * {@link #addNode(NodeId)} instead. Modifying the returned node's
     * properties will take immediate effect in the backing repository.
     */
    Node addNode(NodeId nodeId, int index);

    /**
     * Removes a node, returning an immutable version of the node.
     */
    Node removeNode(NodeId nodeId);

    /**
     * Gets an edge. Modifying the returned edges's properties will take
     * immediate effect in the backing repository.
     */
    Edge getEdge(EdgeId edgeId);

    /**
     * Adds an edge. Modifying the returned edges's properties will take
     * immediate effect in the backing repository.
     */
    Edge addEdge(NodeId startNode, NodeId endNode, EdgeType edgeType);

    /**
     * Adds a weighted edge. Modifying the returned edges's properties will take
     * immediate effect in the backing repository.
     */
    Edge addEdge(NodeId startNode, NodeId endNode, EdgeType edgeType, float weight);

    /**
     * Adds a weighted edge. Modifying the returned edges's properties will take
     * immediate effect in the backing repository.
     */
    Edge addEdge(EdgeId edgeId, int startNodeIndex, int endNodeIndex, float weight);

    /**
     * Removes an edge, returning an immutable version of the edge.
     */
    Edge removeEdge(EdgeId edgeId);

    /**
     * Updates the weight for an edge.
     */
    void setEdgeWeight(EdgeId edge, float weight);

    /**
     * Sets properties for a node. Any previous properties will be removed.
     */
    void setNodeProperties(NodeId nodeId, Properties properties);

    /**
     * Sets properties for an edge. Any previous properties will be removed.
     */
    void setEdgeProperties(EdgeId edgeId, Properties properties);

    /**
     * Exports the graph to file as json.
     */
    void exportJson(File out, boolean includeNodeProperties,
                    boolean includeEdgeProperties);

    /**
     * Imports the graph from a json file.
     */
    void importJson(File in);

    /**
     * Returns all nodes in this graph.
     */
    IterablePipe<Node> getNodes();

    /**
     * Returns all edges in this graph.
     */
    IterablePipe<Edge> getEdges();
}
