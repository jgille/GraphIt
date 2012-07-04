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

import org.graphit.graph.edge.domain.Edge;
import org.graphit.graph.edge.domain.EdgeId;
import org.graphit.graph.edge.schema.EdgeType;
import org.graphit.graph.node.domain.Node;
import org.graphit.graph.node.domain.NodeId;
import org.graphit.graph.schema.GraphMetadata;
import org.graphit.graph.traversal.EdgeDirection;
import org.graphit.properties.domain.Properties;
import org.springframework.core.convert.converter.Converter;

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
     * Gets an iterable of edges connected to a node.
     */
    Iterable<Edge> getEdges(NodeId node, EdgeType edgeType, EdgeDirection direction);

    /**
     * Gets an iterable of edges connected to a node. Each edge will be (lazily)
     * converted using the converter.
     */
    <E> Iterable<E> getAndConvertEdges(NodeId node, EdgeType edgeType, EdgeDirection direction,
                                       Converter<Edge, E> converter);

    /**
     * Gets an iterable of neighbors for a node.
     */
    Iterable<Node> getNeighbors(NodeId node, EdgeType edgeType, EdgeDirection direction);

    /**
     * Gets an iterable of converted neighbors for a node. Each neighbor will be
     * (lazily) converted using the converter.
     */
    <N> Iterable<N> getAndConvertNeighbors(NodeId node, EdgeType edgeType, EdgeDirection direction,
                                           Converter<Node, N> converter);

    /**
     * Gets a node by it's index.
     */
    Node getNode(int index);

    /**
     * Gets a node by it's id.
     */
    Node getNode(NodeId nodeId);

    /**
     * Adds a node.
     */
    Node addNode(NodeId nodeId);

    /**
     * Removes a node.
     */
    Node removeNode(NodeId nodeId);

    /**
     * Gets an edge:
     */
    Edge getEdge(EdgeId edgeId);

    /**
     * Adds an edge.
     */
    Edge addEdge(NodeId startNode, NodeId endNode, EdgeType edgeType);

    /**
     * Adds a weighted edge.
     */
    Edge addEdge(NodeId startNode, NodeId endNode, EdgeType edgeType, float weight);

    /**
     * Removes an edge.
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
    void exportJson(File out, boolean includeProperties);

    /**
     * Imports the graph from a json file.
     */
    void importJson(File in);
}
