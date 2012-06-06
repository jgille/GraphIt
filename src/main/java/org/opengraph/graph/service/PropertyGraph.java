package org.opengraph.graph.service;

import org.opengraph.graph.edge.domain.Edge;
import org.opengraph.graph.edge.domain.EdgeDirection;
import org.opengraph.graph.edge.domain.EdgeId;
import org.opengraph.graph.edge.schema.EdgeType;
import org.opengraph.graph.node.domain.Node;
import org.opengraph.graph.node.domain.NodeId;
import org.opengraph.graph.repository.GraphRepository;
import org.opengraph.graph.schema.GraphMetadata;
import org.opengraph.properties.domain.Properties;
import org.springframework.core.convert.converter.Converter;

import com.tinkerpop.blueprints.Graph;

/**
 * A graph containing nodes connected by edges. Both nodes and edges can have
 * properties associated with them.
 *
 * @author jon
 *
 */
public interface PropertyGraph extends GraphRepository {

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
     * Gets a blueprints compatible view of this graph.
     */
    Graph asBlueprintsGraph();

}
