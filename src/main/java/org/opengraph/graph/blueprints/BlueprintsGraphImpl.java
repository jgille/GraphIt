package org.opengraph.graph.blueprints;

import org.opengraph.graph.edge.domain.EdgeId;
import org.opengraph.graph.edge.schema.EdgeType;
import org.opengraph.graph.node.domain.Node;
import org.opengraph.graph.node.domain.NodeId;
import org.opengraph.graph.service.PropertyGraph;
import org.opengraph.graph.traversal.EdgeDirection;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.Assert;

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
public class BlueprintsGraphImpl implements Graph {

    private final PropertyGraph graphService;
    private final Features features;
    private final BlueprintsEdgesRepository edgesRepo;

    /**
     * Creates a new instance.
     * 
     * @param graphService
     *            The wrapped property graph.
     */
    public BlueprintsGraphImpl(PropertyGraph graphService) {
        this.graphService = graphService;
        this.features = new Features(); // TODO: Set features fields
        this.edgesRepo = new BlueprintsEdgesRepositoryImpl(this);
    }

    /**
     * Converts an edge label to an edge type.
     */
    private EdgeType getEdgeType(String edgeLabel) {
        return graphService.getMetadata().getEdgeTypes().valueOf(edgeLabel);
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

    private NodeId castNodeId(Object nodeId) {
        Assert.isInstanceOf(NodeId.class, nodeId, "Vertex ids must be instances of NodeId.");
        return (NodeId) nodeId;
    }

    private EdgeId castEdgeId(Object edgeId) {
        Assert.isInstanceOf(EdgeId.class, edgeId, "Edge ids must be instances of EdgeId.");
        return (EdgeId) edgeId;
    }

    @Override
    public Features getFeatures() {
        return features;
    }

    @Override
    public Vertex addVertex(Object id) {
        NodeId nodeId = castNodeId(id);
        Node node = graphService.addNode(nodeId);
        Vertex vertex = new BlueprintsNode(node, edgesRepo);
        return vertex;
    }

    @Override
    public Vertex getVertex(Object id) {
        NodeId nodeId = castNodeId(id);
        Node node = graphService.getNode(nodeId);
        return getVertexFromNode(node);
    }

    protected Vertex getVertexFromNode(Node node) {
        Vertex vertex = new BlueprintsNode(node, edgesRepo);
        return vertex;
    }

    @Override
    public void removeVertex(Vertex vertex) {
        NodeId nodeId = castNodeId(vertex.getId());
        graphService.removeNode(nodeId);
    }

    @Override
    public Iterable<Vertex> getVertices() {
        throw new UnsupportedOperationException("Vertex iteration is not supported (yet)");
    }

    @Override
    public Iterable<Vertex> getVertices(String key, Object value) {
        throw new UnsupportedOperationException("Vertex iteration is not supported (yet)");
    }

    @Override
    public Edge addEdge(Object ignored, Vertex outVertex, Vertex inVertex, String label) {
        EdgeType edgeType = getEdgeType(label);
        NodeId startNodeId = castNodeId(outVertex.getId());
        NodeId endNodeId = castNodeId(inVertex.getId());
        org.opengraph.graph.edge.domain.Edge edge =
            graphService.addEdge(startNodeId, endNodeId, edgeType, 0);
        return new BlueprintsEdge(edge.getEdgeId(), outVertex, inVertex, edge);
    }

    @Override
    public Edge getEdge(Object id) {
        EdgeId edgeId = castEdgeId(id);
        org.opengraph.graph.edge.domain.Edge edge = graphService.getEdge(edgeId);
        NodeId startNodeId = edge.getStartNode().getNodeId();
        Vertex startNode = getVertex(startNodeId);
        NodeId endNodeId = edge.getEndNode().getNodeId();
        Vertex endNode = getVertex(endNodeId);
        return new BlueprintsEdge(edgeId, startNode, endNode, edge);
    }

    @Override
    public void removeEdge(Edge edge) {
        EdgeId edgeId = castEdgeId(edge.getId());
        graphService.removeEdge(edgeId);
    }

    @Override
    public Iterable<Edge> getEdges() {
        throw new UnsupportedOperationException("Edge iteration is not supported (yet)");
    }

    @Override
    public Iterable<Edge> getEdges(String key, Object value) {
        throw new UnsupportedOperationException("Edge iteration is not supported (yet)");
    }

    protected PropertyGraph getGraphService() {
        return graphService;
    }

    @Override
    public void shutdown() {
        graphService.shutdown();
    }

    @Override
    public String toString() {
        return "BlueprintsGraphImpl [graphService=" + graphService + "]";
    }

    private static final class BlueprintsEdgesRepositoryImpl implements BlueprintsEdgesRepository {

        private final BlueprintsGraphImpl graph;
        private final Converter<org.opengraph.graph.edge.domain.Edge, Edge> edgeConverter;
        private final Converter<Node, Vertex> vertexConverter;

        private BlueprintsEdgesRepositoryImpl(BlueprintsGraphImpl graph) {
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
    }

    private static final class EdgeConverter implements
        Converter<org.opengraph.graph.edge.domain.Edge, Edge> {

        private final BlueprintsGraphImpl graph;

        private EdgeConverter(BlueprintsGraphImpl graph) {
            this.graph = graph;
        }

        @Override
        public Edge convert(org.opengraph.graph.edge.domain.Edge edge) {
            Node startNode = edge.getStartNode();
            Vertex startVertex = graph.getVertexFromNode(startNode);
            Node endNode = edge.getEndNode();
            Vertex endVertex = graph.getVertexFromNode(endNode);
            return new BlueprintsEdge(edge.getEdgeId(), startVertex, endVertex, edge);
        }

    }

    private static final class VertexConverter implements Converter<Node, Vertex> {

        private final BlueprintsGraphImpl graph;

        private VertexConverter(BlueprintsGraphImpl graph) {
            this.graph = graph;
        }

        @Override
        public Vertex convert(Node node) {
            return graph.getVertexFromNode(node);
        }

    }
}
