package org.opengraph.graph.service;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import org.opengraph.common.collections.CombinedIterable;
import org.opengraph.common.converters.IdentityConverter;
import org.opengraph.graph.blueprints.BlueprintsGraphImpl;
import org.opengraph.graph.edge.domain.Edge;
import org.opengraph.graph.edge.domain.EdgeId;
import org.opengraph.graph.edge.domain.EdgeImpl;
import org.opengraph.graph.edge.domain.EdgePrimitive;
import org.opengraph.graph.edge.domain.EdgeVector;
import org.opengraph.graph.edge.repository.ByteBufferEdgePrimitivesRepository;
import org.opengraph.graph.edge.repository.EdgePrimitivesRepository;
import org.opengraph.graph.edge.schema.EdgeType;
import org.opengraph.graph.node.domain.Node;
import org.opengraph.graph.node.domain.NodeId;
import org.opengraph.graph.node.domain.NodeImpl;
import org.opengraph.graph.node.repository.NodeIdRepository;
import org.opengraph.graph.node.repository.NodeIdRepositoryImpl;
import org.opengraph.graph.repository.AbstractGraphRepository;
import org.opengraph.graph.schema.GraphMetadata;
import org.opengraph.graph.traversal.EdgeDirection;
import org.opengraph.properties.domain.MapProperties;
import org.opengraph.properties.domain.Properties;
import org.opengraph.properties.repository.AlwaysEmptyPropertiesRepository;
import org.opengraph.properties.repository.PropertiesRepository;
import org.opengraph.properties.repository.WriteThroughProperties;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.Assert;

import com.tinkerpop.blueprints.Graph;

public class PropertyGraphImpl extends AbstractGraphRepository implements PropertyGraph {

    private final GraphMetadata metadata;

    private NodeIdRepository nodeRepo;
    private PropertiesRepository<NodeId> nodePropertiesRepo;

    private EdgePrimitivesRepository edgeRepo;
    private PropertiesRepository<EdgeId> edgePropertiesRepo;

    public PropertyGraphImpl(GraphMetadata metadata) {
        this.metadata = metadata;
        this.nodeRepo = new NodeIdRepositoryImpl(metadata.getNodeTypes());
        this.edgeRepo = new ByteBufferEdgePrimitivesRepository(metadata.getEdgeTypes());
        this.nodePropertiesRepo = new AlwaysEmptyPropertiesRepository<NodeId>();
        this.edgePropertiesRepo = new AlwaysEmptyPropertiesRepository<EdgeId>();
    }

    public void setNodeRepo(NodeIdRepository nodeRepo) {
        this.nodeRepo = nodeRepo;
    }

    public void setEdgeRepo(EdgePrimitivesRepository edgeRepo) {
        this.edgeRepo = edgeRepo;
    }

    public void setNodePropertiesRepo(PropertiesRepository<NodeId> nodePropertiesRepo) {
        this.nodePropertiesRepo = nodePropertiesRepo;
    }

    public void setEdgePropertiesRepo(PropertiesRepository<EdgeId> edgePropertiesRepo) {
        this.edgePropertiesRepo = edgePropertiesRepo;
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
                return converter.convert(edge.getOppositeNode(node));
            }
        };
        return getAndConvert(node, edgeType, direction, edgeIdConverter);
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
            new WriteThroughProperties<NodeId>(nodeId, new MapProperties(), nodePropertiesRepo);
        return new NodeImpl(index, nodeId, properties);
    }

    @Override
    public Node getNode(NodeId nodeId) {
        int index = nodeRepo.getNodeIndex(nodeId);
        if (index < 0) {
            return null;
        }
        Properties properties = nodePropertiesRepo.getProperties(nodeId);
        return new NodeImpl(index, nodeId, new WriteThroughProperties<NodeId>(nodeId, properties,
                                                                              nodePropertiesRepo));
    }

    @Override
    public Node getNode(int index) {
        NodeId nodeId = nodeRepo.getNodeId(index);
        if (nodeId == null) {
            return null;
        }
        Properties properties = nodePropertiesRepo.getProperties(nodeId);
        return new NodeImpl(index, nodeId, new WriteThroughProperties<NodeId>(nodeId, properties,
                                                                              nodePropertiesRepo));
    }

    @Override
    public Node removeNode(NodeId nodeId) {
        int index = nodeRepo.getNodeIndex(nodeId);
        if (index < 0) {
            return null;
        }
        edgeRepo.removeNode(index);
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
        Node startNode = getNode(edgePrimitive.getStartNodeId());
        Node endNode = getNode(edgePrimitive.getEndNodeId());
        Properties properties = edgePropertiesRepo.getProperties(edgeId);
        EdgeImpl edge = new EdgeImpl(edgePrimitive.getId(), edgePrimitive.getEdgeType());

        edge.setStartNode(startNode)
            .setEndNode(endNode)
            .setWeight(edgePrimitive.getWeight())
            .setProperties(new WriteThroughProperties<EdgeId>(edgeId, properties,
                                                              edgePropertiesRepo));
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
        EdgeImpl edge = new EdgeImpl(edgeId.getIndex(), edgeType);

        edge.setStartNode(startNode)
            .setEndNode(endNode)
            .setWeight(weight)
            .setProperties(new WriteThroughProperties<EdgeId>(edgeId, new MapProperties(),
                                                              edgePropertiesRepo));
        return edge;
    }

    @Override
    public Edge removeEdge(EdgeId edgeId) {
        EdgePrimitive edgePrimitive = edgeRepo.removeEdge(edgeId);
        if (edgePrimitive == null) {
            return null;
        }
        Node startNode = getNode(edgePrimitive.getStartNodeId());
        Node endNode = getNode(edgePrimitive.getEndNodeId());
        Properties properties = edgePropertiesRepo.removeProperties(edgeId);
        EdgeImpl edge = new EdgeImpl(edgePrimitive.getId(), edgePrimitive.getEdgeType());

        edge.setStartNode(startNode)
            .setEndNode(endNode)
            .setWeight(edgePrimitive.getWeight())
            .setProperties(properties);
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
    public Graph asBlueprintsGraph() {
        return new BlueprintsGraphImpl(this);
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
        // TODO Auto-generated method stub

    }

    @Override
    public void shutdown() {
        // TODO Auto-generated method stub
    }

    @Override
    public String getDirectory() {
        return getBaseDirectory();
    }

    @Override
    protected String getFileName() {
        return "metadata.json";
    }

    @Override
    public void dump(File out) throws IOException {
        // TODO Auto-generated method stub

    }

    @Override
    public void restore(File in) throws IOException {
        // TODO Auto-generated method stub

    }

}
