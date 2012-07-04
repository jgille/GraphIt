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
import java.util.Arrays;
import java.util.Collections;

import org.graphit.common.collections.CombinedIterable;
import org.graphit.common.converters.IdentityConverter;
import org.graphit.graph.edge.domain.Edge;
import org.graphit.graph.edge.domain.EdgeId;
import org.graphit.graph.edge.domain.EdgeImpl;
import org.graphit.graph.edge.domain.EdgePrimitive;
import org.graphit.graph.edge.domain.EdgeVector;
import org.graphit.graph.edge.repository.EdgePrimitivesRepository;
import org.graphit.graph.edge.repository.EdgePrimitivesRepositoryImpl;
import org.graphit.graph.edge.schema.EdgeType;
import org.graphit.graph.node.domain.Node;
import org.graphit.graph.node.domain.NodeId;
import org.graphit.graph.node.domain.NodeImpl;
import org.graphit.graph.node.repository.NodeIdRepository;
import org.graphit.graph.node.repository.NodeIdRepositoryImpl;
import org.graphit.graph.schema.GraphMetadata;
import org.graphit.graph.traversal.EdgeDirection;
import org.graphit.properties.domain.Properties;
import org.graphit.properties.repository.ConcurrentHashMapPropertiesRepository;
import org.graphit.properties.repository.PropertiesRepository;
import org.graphit.properties.repository.WriteThroughProperties;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.Assert;

/**
 * {@link PropertyGraph} implementation.
 *
 * @author jon
 *
 */
public class PropertyGraphImpl implements PropertyGraph {

    private static final int DEFAULT_NODE_CAPACITY = 16;
    private static final int DEFAULT_EDGE_CAPACITY = 16;

    private final GraphMetadata metadata;

    private NodeIdRepository nodeRepo;
    private PropertiesRepository<NodeId> nodePropertiesRepo;

    private EdgePrimitivesRepository edgeRepo;
    private PropertiesRepository<EdgeId> edgePropertiesRepo;

    /**
     * Creates a new graph.
     */
    public PropertyGraphImpl(GraphMetadata metadata) {
        this.metadata = metadata;
        this.nodeRepo = new NodeIdRepositoryImpl();
        this.edgeRepo = new EdgePrimitivesRepositoryImpl(metadata.getEdgeTypes());
        this.nodePropertiesRepo =
            new ConcurrentHashMapPropertiesRepository<NodeId>(DEFAULT_NODE_CAPACITY);
        this.edgePropertiesRepo =
            new ConcurrentHashMapPropertiesRepository<EdgeId>(DEFAULT_EDGE_CAPACITY);
    }

    /**
     * Sets a custom repo used to handle node ids and indexes.
     */
    public void setNodeRepo(NodeIdRepository nodeRepo) {
        this.nodeRepo = nodeRepo;
    }

    /**
     * Gets the repo used to handle node ids and indexes.
     */
    NodeIdRepository getNodeRepo() {
        return nodeRepo;
    }

    /**
     * Sets a custom repo used to handle edge primitives.
     */
    public void setEdgeRepo(EdgePrimitivesRepository edgeRepo) {
        this.edgeRepo = edgeRepo;
    }

    /**
     * Gets the repo used to handle edge primitives.
     */
    EdgePrimitivesRepository getEdgeRepo() {
        return edgeRepo;
    }

    /**
     * Sets a custom repo used to handle node properties.
     */
    public void setNodePropertiesRepo(PropertiesRepository<NodeId> nodePropertiesRepo) {
        this.nodePropertiesRepo = nodePropertiesRepo;
    }

    /**
     * Gets the repo used to handle node properties.
     */
    PropertiesRepository<NodeId> getNodePropertiesRepo() {
        return nodePropertiesRepo;
    }

    /**
     * Sets a custom repo used to handle edge properties.
     */
    public void setEdgePropertiesRepo(PropertiesRepository<EdgeId> edgePropertiesRepo) {
        this.edgePropertiesRepo = edgePropertiesRepo;
    }

    /**
     * Gets the repo used to handle edge properties.
     */
    PropertiesRepository<EdgeId> getEdgePropertiesRepo() {
        return edgePropertiesRepo;
    }

    @Override
    public GraphMetadata getMetadata() {
        return metadata;
    }

    private int getNodeIndex(NodeId nodeId) {
        return nodeRepo.getNodeIndex(nodeId);
    }

    @Override
    public Iterable<Edge>
        getEdges(NodeId node, EdgeType edgeType, EdgeDirection direction) {
        return getAndConvertEdges(node, edgeType, direction, new IdentityConverter<Edge>());
    }

    @Override
    public <E> Iterable<E> getAndConvertEdges(NodeId node, EdgeType edgeType,
                                              EdgeDirection direction,
                                              final Converter<Edge, E> converter) {
        Converter<EdgeId, E> edgeIdConverter = new Converter<EdgeId, E>() {

            @Override
            public E convert(EdgeId edgeId) {
                Edge edge = getEdge(edgeId);
                if (!isValidEdge(edge)) {
                    return null;
                }
                return converter.convert(edge);
            }
        };
        return getAndConvert(node, edgeType, direction, edgeIdConverter);
    }

    @Override
    public Iterable<Node> getNeighbors(NodeId node, EdgeType edgeType, EdgeDirection direction) {
        return getAndConvertNeighbors(node, edgeType, direction, new IdentityConverter<Node>());
    }

    @Override
    public <N> Iterable<N> getAndConvertNeighbors(final NodeId node, EdgeType edgeType,
                                                  EdgeDirection direction,
                                                  final Converter<Node, N> converter) {
        Converter<EdgeId, N> edgeIdConverter = new Converter<EdgeId, N>() {

            @Override
            public N convert(EdgeId edgeId) {
                Edge edge = getEdge(edgeId);
                if (!isValidEdge(edge)) {
                    return null;
                }
                return converter.convert(edge.getOppositeNode(node));
            }
        };
        return getAndConvert(node, edgeType, direction, edgeIdConverter);
    }

    private boolean isValidEdge(Edge edge) {
        return edge != null;
    }

    @SuppressWarnings("unchecked")
    private <T> Iterable<T> getAndConvert(NodeId node, EdgeType edgeType,
                                          EdgeDirection direction,
                                          Converter<EdgeId, T> converter) {

        int nodeIndex = getNodeIndex(node);
        if (nodeIndex < 0) {
            return Collections.emptyList();
        }
        Iterable<T> iterable;
        switch (direction) {
        case BOTH:
            EdgeVector outgoing = edgeRepo.getOutgoingEdges(nodeIndex, edgeType);
            EdgeVector incoming = edgeRepo.getIncomingEdges(nodeIndex, edgeType);
            Iterable<T> outIterable = outgoing.iterable(converter);
            Iterable<T> inIterable = incoming.iterable(converter);
            iterable = new CombinedIterable<T>(Arrays.asList(outIterable, inIterable));
            break;
        case OUTGOING:
            EdgeVector oEdges =
                edgeRepo.getOutgoingEdges(nodeIndex, edgeType);
            iterable = oEdges.iterable(converter);
            break;
        case INCOMING:
            EdgeVector iEdges =
                edgeRepo.getIncomingEdges(nodeIndex, edgeType);
            iterable = iEdges.iterable(converter);
            break;
        default:
            throw new IllegalArgumentException("Illegal direction: " + direction);
        }
        return iterable;

    }

    @Override
    public Node addNode(NodeId nodeId) {
        int index = nodeRepo.insert(nodeId);
        Properties properties =
            new WriteThroughProperties<NodeId>(nodeId, nodePropertiesRepo);
        return new NodeImpl(index, nodeId, properties);
    }

    @Override
    public Node getNode(NodeId nodeId) {
        int index = nodeRepo.getNodeIndex(nodeId);
        if (index < 0) {
            return null;
        }
        return new NodeImpl(index, nodeId, new WriteThroughProperties<NodeId>(nodeId,
                                                                              nodePropertiesRepo));
    }

    @Override
    public Node getNode(int index) {
        NodeId nodeId = nodeRepo.getNodeId(index);
        if (nodeId == null) {
            return null;
        }
        return new NodeImpl(index, nodeId, new WriteThroughProperties<NodeId>(nodeId,
                                                                              nodePropertiesRepo));
    }

    @Override
    public Node removeNode(NodeId nodeId) {
        int index = nodeRepo.getNodeIndex(nodeId);
        if (index < 0) {
            return null;
        }
        nodeRepo.remove(index);
        Properties properties;
        properties = nodePropertiesRepo.removeProperties(nodeId);
        NodeImpl node = new NodeImpl(index, nodeId, properties);
        return node.immutableNode();
    }

    @Override
    public Edge getEdge(EdgeId edgeId) {
        EdgePrimitive edgePrimitive = edgeRepo.getEdge(edgeId);
        if (edgePrimitive == null) {
            return null;
        }
        Node startNode = getNode(edgePrimitive.getStartNodeIndex());
        Node endNode = getNode(edgePrimitive.getEndNodeIndex());
        if (startNode == null || endNode == null) {
            return null;
        }
        EdgeImpl edge = new EdgeImpl(edgePrimitive.getIndex(), edgePrimitive.getEdgeType(),
                                     new WriteThroughProperties<EdgeId>(edgeId,
                                                                        edgePropertiesRepo));

        edge.setStartNode(startNode)
            .setEndNode(endNode)
            .setWeight(edgePrimitive.getWeight());
        return edge;
    }

    @Override
    public Edge addEdge(NodeId startNodeId, NodeId endNodeId, EdgeType edgeType) {
        return doAddEdge(startNodeId, endNodeId, edgeType, 0);
    }

    @Override
    public Edge addEdge(NodeId startNodeId, NodeId endNodeId, EdgeType edgeType, float weight) {
        Assert.isTrue(edgeType.isWeighted(), edgeType + " is unweughted.");
        return doAddEdge(startNodeId, endNodeId, edgeType, weight);
    }

    private Edge doAddEdge(NodeId startNodeId, NodeId endNodeId, EdgeType edgeType, float weight) {
        Node startNode = getNode(startNodeId);
        Assert.notNull(startNode, "Invalid start node: " + startNodeId);
        Node endNode = getNode(endNodeId);
        Assert.notNull(endNode, "Invalid end node: " + endNodeId);

        EdgeId edgeId;
        if (edgeType.isWeighted()) {
            edgeId =
                edgeRepo.addWeightedEdge(startNode.getIndex(), endNode.getIndex(), edgeType,
                                          weight);
        } else {
            edgeId = edgeRepo.addEdge(startNode.getIndex(), endNode.getIndex(), edgeType);
        }
        EdgeImpl edge =
            new EdgeImpl(edgeId.getIndex(), edgeType,
                         new WriteThroughProperties<EdgeId>(edgeId,
                                                            edgePropertiesRepo));

        edge.setStartNode(startNode)
            .setEndNode(endNode)
            .setWeight(weight);
        return edge;
    }

    @Override
    public Edge removeEdge(EdgeId edgeId) {
        EdgePrimitive edgePrimitive = edgeRepo.removeEdge(edgeId);
        if (edgePrimitive == null) {
            return null;
        }
        Properties properties = edgePropertiesRepo.removeProperties(edgeId);
        EdgeImpl edge =
            new EdgeImpl(edgePrimitive.getIndex(), edgePrimitive.getEdgeType(), properties);
        return edge;
    }

    @Override
    public void setEdgeWeight(EdgeId edgeId, float weight) {
        edgeRepo.setEdgeWeight(edgeId, weight);
    }

    @Override
    public String toString() {
        return "PropertyGraphImpl [metadata=" + metadata + "]";
    }

    @Override
    public void setNodeProperties(NodeId nodeId, Properties properties) {
        nodePropertiesRepo.saveProperties(nodeId, properties);
    }

    @Override
    public void setEdgeProperties(EdgeId edgeId, Properties properties) {
        edgePropertiesRepo.saveProperties(edgeId, properties);
    }

    @Override
    public void init() {
        // TODO: Implement
    }

    @Override
    public void shutdown() {
        // TODO: Implement
    }

    @Override
    public void exportJson(File out, boolean includeProperties) {
        // TODO: Implement
    }

    @Override
    public void importJson(File in) {
        // TODO: Implement
    }
}
