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

package org.graphit.graph.blueprints;

import java.util.Collection;
import java.util.UUID;

import org.graphit.graph.edge.domain.EdgeId;
import org.graphit.graph.edge.schema.EdgeType;
import org.graphit.graph.edge.schema.EdgeTypeImpl;
import org.graphit.graph.edge.schema.EdgeTypes;
import org.graphit.graph.node.domain.Node;
import org.graphit.graph.node.domain.NodeId;
import org.graphit.graph.node.schema.NodeType;
import org.graphit.graph.node.schema.NodeTypeImpl;
import org.graphit.graph.service.PropertyGraph;
import org.graphit.graph.traversal.EdgeDirection;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.Assert;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Features;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;

/**
 * A {@link Graph} implementation wrapping a {@link PropertyGraph}.
 *
 * @author jon
 *
 */
public class BlueprintsGraph implements Graph {

    private final PropertyGraph graph;
    private final Features graphFeatures;
    private final BlueprintsEdgesRepository edgesRepo;

    static final NodeType DEFAULT_NODE_TYPE = new NodeTypeImpl("_default_node_type");
    static final EdgeType DEFAULT_EDGE_TYPE = new EdgeTypeImpl("_default_edge_type");

    /**
     * Creates a new instance.
     *
     * @param graph
     *            The wrapped property graph.
     */
    public BlueprintsGraph(PropertyGraph graph) {
        this.graph = graph;
        this.graphFeatures = setupFeatures();
        this.edgesRepo = new BlueprintsEdgesRepositoryImpl(this);
    }

    private Features setupFeatures() {
        Features features = new Features();
        features.supportsDuplicateEdges = Boolean.TRUE;
        features.supportsSelfLoops = Boolean.TRUE;
        features.supportsSerializableObjectProperty = Boolean.TRUE;
        features.supportsBooleanProperty = Boolean.TRUE;
        features.supportsDoubleProperty = Boolean.TRUE;
        features.supportsFloatProperty = Boolean.TRUE;
        features.supportsIntegerProperty = Boolean.TRUE;
        features.supportsPrimitiveArrayProperty = Boolean.TRUE;
        features.supportsUniformListProperty = Boolean.TRUE;
        features.supportsMixedListProperty = Boolean.TRUE;
        features.supportsLongProperty = Boolean.TRUE;
        features.supportsMapProperty = Boolean.TRUE;
        features.supportsStringProperty = Boolean.TRUE;
        features.ignoresSuppliedIds = Boolean.FALSE;
        features.isPersistent = Boolean.FALSE;
        features.isRDFModel = Boolean.FALSE;
        features.isWrapper = Boolean.FALSE;
        features.supportsIndices = Boolean.FALSE;
        features.supportsVertexIndex = Boolean.FALSE;
        features.supportsEdgeIndex = Boolean.FALSE;
        features.supportsKeyIndices = Boolean.FALSE;
        features.supportsVertexKeyIndex = Boolean.FALSE;
        features.supportsEdgeKeyIndex = Boolean.FALSE;
        features.supportsEdgeIteration = Boolean.TRUE;
        features.supportsVertexIteration = Boolean.TRUE;
        features.supportsTransactions = Boolean.FALSE;
        features.supportsThreadedTransactions = Boolean.FALSE;

        return features;
    }

    /**
     * Converts an edge label to an edge type.
     */
    private EdgeType getEdgeType(String edgeLabel) {
        return graph.getMetadata().getEdgeTypes().valueOf(edgeLabel);
    }

    /**
     * Converts a {@link Direction} to an {@link EdgeDirection}.
     *
     */
    EdgeDirection getEdgeDirection(Direction direction) {
        switch (direction) {
        case OUT:
            return EdgeDirection.OUTGOING;
        case IN:
            return EdgeDirection.INCOMING;
        case BOTH:
            return EdgeDirection.BOTH;
        default:
            throw new IllegalArgumentException("Illegal direction.");
        }
    }

    private NodeId getNodeId(Object nodeId) {
        if (nodeId instanceof NodeId) {
            return (NodeId) nodeId;
        }
        if (nodeId == null) {
            return new NodeId(DEFAULT_NODE_TYPE, UUID.randomUUID().toString());
        }
        // TODO: Check if nodeId is a String matching a pattern like
        // "some_node_type:some_id"
        return new NodeId(DEFAULT_NODE_TYPE, nodeId.toString());
    }

    private EdgeId castEdgeId(Object edgeId) {
        if (edgeId instanceof EdgeId) {
            return (EdgeId) edgeId;
        }
        // TODO: Check if edgeId is a String matching a pattern like
        // "some_edge_type:index"
        return new EdgeId(DEFAULT_EDGE_TYPE, Integer.MAX_VALUE);
    }

    @Override
    public Features getFeatures() {
        return graphFeatures;
    }

    @Override
    public Vertex addVertex(Object id) {
        NodeId nodeId = getNodeId(id);
        Node node = graph.addNode(nodeId);
        return transformNode(node);
    }

    @Override
    public Vertex getVertex(Object id) {
        Assert.notNull(id);
        NodeId nodeId = getNodeId(id);
        Node node = graph.getNode(nodeId);
        return transformNode(node);
    }

    protected Vertex transformNode(Node node) {
        if (node == null) {
            return null;
        }
        return new BlueprintsNode(node, edgesRepo);
    }

    @Override
    public void removeVertex(Vertex vertex) {
        NodeId nodeId = getNodeId(vertex.getId());
        graph.removeNode(nodeId);
    }

    @Override
    public Iterable<Vertex> getVertices() {
        Iterable<Node> nodes = graph.getNodes();
        return Iterables.transform(nodes, new Function<Node, Vertex>() {

            @Override
            public Vertex apply(Node node) {
                return transformNode(node);
            }
        });
    }

    @Override
    public Iterable<Vertex> getVertices(final String key, final Object value) {
        Iterable<Vertex> vertices = getVertices();
        return Iterables.filter(vertices, new Predicate<Vertex>() {

            @Override
            public boolean apply(Vertex vertex) {
                return value.equals(vertex.getProperty(key));
            }
        });
    }

    @Override
    public Edge addEdge(Object ignored, Vertex outVertex, Vertex inVertex, String label) {
        EdgeType edgeType = getEdgeType(label);
        NodeId startNodeId = getNodeId(outVertex.getId());
        NodeId endNodeId = getNodeId(inVertex.getId());
        org.graphit.graph.edge.domain.Edge edge =
            graph.addEdge(startNodeId, endNodeId, edgeType);
        return new BlueprintsEdge(edge.getEdgeId(), outVertex, inVertex, edge);
    }

    @Override
    public Edge getEdge(Object id) {
        Assert.notNull(id);
        EdgeId edgeId = castEdgeId(id);
        org.graphit.graph.edge.domain.Edge edge = graph.getEdge(edgeId);
        if (edge == null) {
            return null;
        }
        return transformEdge(edge);
    }

    private Edge transformEdge(org.graphit.graph.edge.domain.Edge edge) {
        NodeId startNodeId = edge.getStartNode().getNodeId();
        Vertex startNode = getVertex(startNodeId);
        NodeId endNodeId = edge.getEndNode().getNodeId();
        Vertex endNode = getVertex(endNodeId);
        return new BlueprintsEdge(edge.getEdgeId(), startNode, endNode, edge);
    }

    @Override
    public void removeEdge(Edge edge) {
        EdgeId edgeId = castEdgeId(edge.getId());
        graph.removeEdge(edgeId);
    }

    @Override
    public Iterable<Edge> getEdges() {
        Iterable<org.graphit.graph.edge.domain.Edge> edges = graph.getEdges();
        return Iterables.transform(edges, new Function<org.graphit.graph.edge.domain.Edge, Edge>() {

            @Override
            public Edge apply(org.graphit.graph.edge.domain.Edge edge) {
                return transformEdge(edge);
            }
        });
    }

    @Override
    public Iterable<Edge> getEdges(final String key, final Object value) {
        Iterable<Edge> edges = getEdges();
        return Iterables.filter(edges, new Predicate<Edge>() {

            @Override
            public boolean apply(Edge edge) {
                return value.equals(edge.getProperty(key));
            }
        });
    }

    protected PropertyGraph getGraphService() {
        return graph;
    }

    @Override
    public void shutdown() {
        graph.shutdown();
    }

    @Override
    public String toString() {
        return "blueprintsgraph [graphService=" + graph + "]";
    }

    EdgeTypes getEdgeTypes() {
        return graph.getMetadata().getEdgeTypes();
    }

    private static final class BlueprintsEdgesRepositoryImpl implements BlueprintsEdgesRepository {

        private final BlueprintsGraph graph;
        private final Converter<org.graphit.graph.edge.domain.Edge, Edge> edgeConverter;
        private final Converter<Node, Vertex> vertexConverter;

        private BlueprintsEdgesRepositoryImpl(BlueprintsGraph graph) {
            this.graph = graph;
            this.edgeConverter = new EdgeConverter(graph);
            this.vertexConverter = new VertexConverter(graph);
        }

        @Override
        public Iterable<Edge> getEdges(NodeId nodeId, Direction direction, String edgeLabel) {
            PropertyGraph service = graph.getGraphService();
            EdgeType edgeType = graph.getEdgeType(edgeLabel);
            EdgeDirection edgeDirection = graph.getEdgeDirection(direction);
            Iterable<Edge> edges =
                service.getAndConvertEdges(nodeId, edgeType, edgeDirection, edgeConverter);
            return edges;
        }

        @Override
        public Iterable<Vertex> getNeighbors(NodeId nodeId, Direction direction,
                                             String edgeLabel) {
            PropertyGraph service = graph.getGraphService();
            EdgeType edgeType = graph.getEdgeType(edgeLabel);
            EdgeDirection edgeDirection = graph.getEdgeDirection(direction);
            Iterable<Vertex> vertexes =
                service.getAndConvertNeighbors(nodeId, edgeType, edgeDirection, vertexConverter);
            return vertexes;
        }

        @Override
        public Collection<String> getEdgeTypes() {
            EdgeTypes edgeTypes = graph.getEdgeTypes();
            return Collections2.transform(edgeTypes.elements(), new Function<EdgeType, String>() {

                @Override
                public String apply(EdgeType edgeType) {
                    return edgeType.name();
                }
            });
        }
    }

    private static final class EdgeConverter implements
        Converter<org.graphit.graph.edge.domain.Edge, Edge> {

        private final BlueprintsGraph graph;

        private EdgeConverter(BlueprintsGraph graph) {
            this.graph = graph;
        }

        @Override
        public Edge convert(org.graphit.graph.edge.domain.Edge edge) {
            Node startNode = edge.getStartNode();
            Vertex startVertex = graph.transformNode(startNode);
            Node endNode = edge.getEndNode();
            Vertex endVertex = graph.transformNode(endNode);
            return new BlueprintsEdge(edge.getEdgeId(), startVertex, endVertex, edge);
        }

    }

    private static final class VertexConverter implements Converter<Node, Vertex> {

        private final BlueprintsGraph graph;

        private VertexConverter(BlueprintsGraph graph) {
            this.graph = graph;
        }

        @Override
        public Vertex convert(Node node) {
            return graph.transformNode(node);
        }

    }
}