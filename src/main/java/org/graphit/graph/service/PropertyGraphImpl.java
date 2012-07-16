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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.graphit.common.collections.IterablePipe;
import org.graphit.common.collections.IterablePipeImpl;
import org.graphit.graph.edge.domain.Edge;
import org.graphit.graph.edge.domain.EdgeId;
import org.graphit.graph.edge.domain.EdgeImpl;
import org.graphit.graph.edge.domain.EdgePrimitive;
import org.graphit.graph.edge.domain.EdgeVector;
import org.graphit.graph.edge.repository.EdgePrimitivesRepository;
import org.graphit.graph.edge.repository.EdgePrimitivesRepositoryImpl;
import org.graphit.graph.edge.schema.EdgeSortOrder;
import org.graphit.graph.edge.schema.EdgeType;
import org.graphit.graph.edge.schema.EdgeTypeImpl;
import org.graphit.graph.exception.GraphException;
import org.graphit.graph.node.domain.Node;
import org.graphit.graph.node.domain.NodeId;
import org.graphit.graph.node.domain.NodeImpl;
import org.graphit.graph.node.repository.NodeIdRepository;
import org.graphit.graph.node.repository.NodeIdRepositoryImpl;
import org.graphit.graph.node.schema.NodeType;
import org.graphit.graph.node.schema.NodeTypeImpl;
import org.graphit.graph.schema.GraphMetadata;
import org.graphit.graph.traversal.EdgeDirection;
import org.graphit.properties.domain.Properties;
import org.graphit.properties.repository.ConcurrentHashMapPropertiesRepository;
import org.graphit.properties.repository.PropertiesRepository;
import org.graphit.properties.repository.WriteThroughProperties;
import org.springframework.util.Assert;

import com.google.common.base.Function;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;

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

    private boolean shouldPersistNodeProperties = true;
    private boolean shouldPersistEdgeProperties = true;
    private String dataDir;

    /**
     * Creates a new graph.
     */
    public PropertyGraphImpl() {
        this(new GraphMetadata());
    }

    /**
     * Creates a new graph.
     */
    public PropertyGraphImpl(String graphName) {
        this(new GraphMetadata(graphName));
    }

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
     * Gets the directory, if any, in which this graph is persisted.
     */
    public String getDataDir() {
        return dataDir;
    }

    /**
     * Sets the directory in which this graph is persisted.
     */
    public void setDataDir(String dataDir) {
        this.dataDir = dataDir;
    }

    /**
     * Defines if node properties are to be included when/if the graph is
     * persisted on disk.
     */
    public boolean shouldPersistNodeProperties() {
        return shouldPersistNodeProperties;
    }

    /**
     * Sets the flag that defines if node properties are to be included when/if
     * the graph is persisted on disk.
     */
    public void setShouldPersistNodeProperties(boolean shouldPersistNodeProperties) {
        this.shouldPersistNodeProperties = shouldPersistNodeProperties;
    }

    /**
     * Defines if node properties are to be included when/if the graph is
     * persisted on disk.
     */
    public boolean shouldPersistEdgeProperties() {
        return shouldPersistEdgeProperties;
    }

    /**
     * Sets the flag that defines if node properties are to be included when/if
     * the graph is persisted on disk.
     */
    public void setShouldPersistEdgeProperties(boolean shouldPersistEdgeProperties) {
        this.shouldPersistEdgeProperties = shouldPersistEdgeProperties;
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
    public IterablePipe<Edge>
        getEdges(NodeId nodeId, EdgeType edgeType, EdgeDirection direction) {
        return getEdgeIds(nodeId, edgeType, direction)
            .transform(new Function<EdgeId, Edge>() {

                @Override
                public Edge apply(EdgeId edgeId) {
                    Assert.notNull(edgeId);
                    return getEdge(edgeId);
                }
            }).filter(Predicates.<Edge> notNull());
    }

    @Override
    public IterablePipe<Node> getNeighbors(final NodeId nodeId, EdgeType edgeType,
                                           EdgeDirection direction) {
        return getEdgeIds(nodeId, edgeType, direction)
            .transform(new Function<EdgeId, Node>() {

                @Override
                public Node apply(EdgeId edgeId) {
                    Assert.notNull(edgeId);
                    Edge edge = getEdge(edgeId);
                    if (edge == null) {
                        return null;
                    }
                    return edge.getOppositeNode(nodeId);
                }
            }).filter(Predicates.<Node> notNull());
    }

    private IterablePipe<EdgeId> getEdgeIds(NodeId node, EdgeType edgeType,
                                            EdgeDirection direction) {

        int nodeIndex = getNodeIndex(node);
        if (nodeIndex < 0) {
            return new IterablePipeImpl<EdgeId>();
        }
        IterablePipe<EdgeId> iterable;
        switch (direction) {
        case BOTH:
            EdgeVector outgoing = edgeRepo.getOutgoingEdges(nodeIndex, edgeType);
            EdgeVector incoming = edgeRepo.getIncomingEdges(nodeIndex, edgeType);
            Iterable<EdgeId> outIterable = outgoing.iterable();
            Iterable<EdgeId> inIterable = incoming.iterable();
            iterable = new IterablePipeImpl<EdgeId>(Iterables.concat(outIterable, inIterable));
            break;
        case OUTGOING:
            EdgeVector oEdges =
                edgeRepo.getOutgoingEdges(nodeIndex, edgeType);
            iterable = new IterablePipeImpl<EdgeId>(oEdges.iterable());
            break;
        case INCOMING:
            EdgeVector iEdges =
                edgeRepo.getIncomingEdges(nodeIndex, edgeType);
            iterable = new IterablePipeImpl<EdgeId>(iEdges.iterable());
            break;
        default:
            throw new IllegalArgumentException("Illegal direction: " + direction);
        }
        return iterable;

    }

    private NodeId validateNodeId(NodeId nodeId) {
        Assert.notNull(nodeId, "Node id is mandatory");
        NodeType nodeType = nodeId.getNodeType();
        Assert.notNull(nodeType, "Node type is mandatory.");
        String nodeTypeName = nodeId.getNodeType().name();
        String id = nodeId.getId();
        Assert.notNull(id, "Node type is mandatory.");
        // Make sure we use the same node type instance for all nodes of the
        // same type
        NodeType internedNodeType = metadata.getNodeTypes().valueOf(nodeTypeName);
        return new NodeId(internedNodeType, id);
    }

    @Override
    public Node addNode(NodeId nodeId) {
        NodeId id = validateNodeId(nodeId);
        int index = nodeRepo.insert(id);
        Properties properties =
            new WriteThroughProperties<NodeId>(id, nodePropertiesRepo);
        return new NodeImpl(index, id, properties);
    }

    @Override
    public Node addNode(NodeId nodeId, int index) {
        NodeId id = validateNodeId(nodeId);
        nodeRepo.insert(index, nodeId);
        Properties properties =
            new WriteThroughProperties<NodeId>(id, nodePropertiesRepo);
        return new NodeImpl(index, id, properties);
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

    private EdgeId validateEdgeId(EdgeId edgeId) {
        Assert.notNull(edgeId, "Edge id is mandatory");
        EdgeType edgeType = edgeId.getEdgeType();
        Assert.notNull(edgeType, "Edge type is mandatory.");
        String edgeTypeName = edgeId.getEdgeType().name();
        int index = edgeId.getIndex();
        Assert.isTrue(index >= 0, "Edge indexes may not be negative.");
        // Make sure we use the same node type instance for all nodes of the
        // same type (and assert that the edge type exists).
        EdgeType internedEdgeType = metadata.getEdgeTypes().valueOf(edgeTypeName);
        return new EdgeId(internedEdgeType, index);
    }

    @Override
    public Edge addEdge(NodeId startNodeId, NodeId endNodeId, EdgeType edgeType) {
        return doAddEdge(startNodeId, endNodeId, edgeType, 0);
    }

    @Override
    public Edge addEdge(NodeId startNodeId, NodeId endNodeId, EdgeType edgeType, float weight) {
        return doAddEdge(startNodeId, endNodeId, edgeType, weight);
    }

    @Override
    public Edge addEdge(EdgeId edgeId, int startNodeIndex, int endNodeIndex, float weight) {
        edgeRepo.addEdge(validateEdgeId(edgeId), startNodeIndex, endNodeIndex, weight);
        return getEdge(edgeId);
    }

    private Edge doAddEdge(NodeId startNodeId, NodeId endNodeId, EdgeType edgeType, float weight) {
        // This will make sure the edge type is valid
        validateEdgeId(new EdgeId(edgeType, 0));
        Node startNode = getNode(startNodeId);
        Assert.notNull(startNode, "Invalid start node: " + startNodeId);
        Node endNode = getNode(endNodeId);
        Assert.notNull(endNode, "Invalid end node: " + endNodeId);

        EdgeId edgeId =
            edgeRepo.addEdge(startNode.getIndex(), endNode.getIndex(), edgeType, weight);
        EdgeImpl edge =
            new EdgeImpl(edgeId.getIndex(), edgeType,
                         new WriteThroughProperties<EdgeId>(edgeId, edgePropertiesRepo));

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
        Assert.isTrue(nodeRepo.size() == 0, "You may no call init for a non empty graph.");
        if (dataDir == null) {
            return;
        }
        File file = getFile();
        if (!file.exists()) {
            return;
        }

        importJson(file);

        // Version the file
        try {
            File versionsDir = new File(dataDir, "versions");
            FileUtils.forceMkdir(versionsDir);
            file.renameTo(getVersionedFile(versionsDir));
        } catch (IOException e) {
            throw new GraphException("Failed to create versioned graph file.", e);
        }
    }

    private File getFile() {
        return new File(dataDir, String.format("g-%s.json", metadata.getGraphName()));
    }

    private File getVersionedFile(File versionsDir) {
        return new File(versionsDir, String.format("g-%s.%d.json",
                                                   metadata.getGraphName(),
                                                   System.currentTimeMillis()));
    }

    @Override
    public void shutdown() {
        if (dataDir == null) {
            return;
        }
        File file = getFile();
        exportJson(file, shouldPersistNodeProperties, shouldPersistEdgeProperties);
    }

    @Override
    public void exportJson(File out, boolean includeNodeProperties,
                           boolean includeEdgeProperties) {
        try {
            PropertyGraphJsonUtils.exportJson(this, out, includeNodeProperties,
                                              includeEdgeProperties);
        } catch (IOException e) {
            throw new GraphException("Failed to export graph.", e);
        }
    }

    @Override
    public void importJson(File in) {
        try {
            PropertyGraphJsonUtils.importJson(this, in);
        } catch (IOException e) {
            throw new GraphException("Failed to import graph.", e);
        }
    }

    @Override
    public IterablePipe<Node> getNodes() {
        Iterable<NodeId> nodeIds = nodeRepo.getNodes();
        Iterable<Node> nodes = Iterables.transform(nodeIds, new Function<NodeId, Node>() {

            @Override
            public Node apply(NodeId nodeId) {
                return getNode(nodeId);
            }
        });
        return new IterablePipeImpl<Node>(nodes);
    }

    @Override
    public IterablePipe<Edge> getEdges() {
        Iterable<NodeId> nodeIds = nodeRepo.getNodes();
        List<Iterable<Edge>> edges = new ArrayList<Iterable<Edge>>();
        for (NodeId nodeId : nodeIds) {
            for (EdgeType edgeType : metadata.getEdgeTypes().elements()) {
                Iterable<Edge> outgoingEdges =
                    getEdges(nodeId, edgeType, EdgeDirection.OUTGOING);
                edges.add(outgoingEdges);
            }
        }
        return new IterablePipeImpl<Edge>(Iterables.concat(edges));
    }

    @Override
    public EdgeType createEdgeType(String name) {
        EdgeType edgeType = new EdgeTypeImpl(name);
        metadata.addEdgeType(edgeType);
        return edgeType;
    }

    @Override
    public EdgeType createEdgeType(String name, EdgeSortOrder sortOrder) {
        EdgeType edgeType = new EdgeTypeImpl(name, sortOrder);
        metadata.addEdgeType(edgeType);
        return edgeType;
    }

    @Override
    public NodeType createNodeType(String name) {
        NodeType nodeType = new NodeTypeImpl(name);
        metadata.addNodeType(nodeType);
        return nodeType;
    }

    @Override
    public EdgeType getOrCreateEdgeType(String name) {
        return metadata.getOrCreateEdgeType(name);
    }

    @Override
    public NodeType getOrCreateNodeType(String name) {
        return metadata.getOrCreateNodeType(name);
    }

    @Override
    public void setGraphName(String graphName) {
        metadata.setGraphName(graphName);
    }

    @Override
    public EdgeType getEdgeType(String name) {
        return metadata.getEdgeTypes().valueOf(name);
    }

    @Override
    public NodeType getNodeType(String name) {
        return metadata.getNodeTypes().valueOf(name);
    }
}
