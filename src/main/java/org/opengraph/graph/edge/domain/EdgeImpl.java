package org.opengraph.graph.edge.domain;

import java.util.Map;
import java.util.Set;

import org.opengraph.graph.edge.schema.EdgeType;
import org.opengraph.graph.node.domain.Node;
import org.opengraph.graph.node.domain.NodeId;
import org.opengraph.properties.domain.Properties;
import org.springframework.util.Assert;

/**
 * An edge in a graph.
 *
 * Note: This class is not thread safe.
 *
 * @author jon
 */
public class EdgeImpl implements Edge {

    private final EdgeId edgeId;
    private Node startNode;
    private Node endNode;
    private float weight;
    private Properties properties;

    public EdgeImpl(int id, EdgeType edgeType) {
        this.edgeId = new EdgeId(edgeType, id);
    }

    @Override
    public EdgeType getType() {
        return edgeId.getEdgeType();
    }

    @Override
    public int getIndex() {
        return edgeId.getIndex();
    }

    @Override
    public EdgeId getEdgeId() {
        return edgeId;
    }

    @Override
    public Node getStartNode() {
        return startNode;
    }

    @Override
    public Node getEndNode() {
        return endNode;
    }

    @Override
    public float getWeight() {
        return weight;
    }

    public EdgeImpl setStartNode(Node startNode) {
        Assert.notNull(startNode, "Null start node for edge: " + this);
        this.startNode = startNode;
        return this;
    }

    public EdgeImpl setEndNode(Node endNode) {
        Assert.notNull(endNode, "Null end node for edge: " + this);
        this.endNode = endNode;
        return this;
    }

    public EdgeImpl setWeight(float weight) {
        this.weight = weight;
        return this;
    }

    public EdgeImpl setProperties(Properties properties) {
        this.properties = properties;
        return this;
    }

    @Override
    public int hashCode() {
        return edgeId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        EdgeImpl other = (EdgeImpl) obj;
        return edgeId.equals(other.edgeId);
    }

    @Override
    public String toString() {
        return "EdgeImpl [edgeId=" + edgeId + ", startNode=" + startNode + ", endNode=" + endNode
            + ", weight=" + weight + ", properties=" + properties + "]";
    }

    @Override
    public Object getProperty(String key) {
        return properties.getProperty(key);
    }

    @Override
    public void setProperty(String key, Object value) {
        properties.setProperty(key, value);
    }

    @Override
    public Object removeProperty(String key) {
        return properties.removeProperty(key);
    }

    @Override
    public boolean containsProperty(String key) {
        return properties.containsProperty(key);
    }

    @Override
    public Set<String> getPropertyKeys() {
        return properties.getPropertyKeys();
    }

    @Override
    public Map<String, Object> asPropertyMap() {
        return properties.asPropertyMap();
    }

    @Override
    public Node getOppositeNode(NodeId nodeId) {
        if (nodeId.equals(startNode.getNodeId())) {
            return endNode;
        }
        if (nodeId.equals(endNode.getNodeId())) {
            return startNode;
        }
        throw new IllegalArgumentException(nodeId + " is not a start or end node for this edge: "
            + startNode.getNodeId() + " -> " + endNode.getNodeId());
    }

}
