package org.opengraph.graph.node.domain;

import org.opengraph.graph.node.schema.NodeType;
import org.opengraph.properties.domain.Properties;
import org.opengraph.properties.domain.PropertiesProxy;
import org.springframework.util.Assert;

/**
 * Standard {@link Node} implementation.
 *
 * @author jon
 *
 */
public class NodeImpl extends PropertiesProxy implements Node {

    private final int nodeIndex;
    private final NodeId nodeId;

    /**
     * Creates a new node.
     */
    public NodeImpl(int nodeIndex, NodeId nodeId, Properties properties) {
        this(nodeIndex, nodeId, properties, true);
    }

    private NodeImpl(int nodeIndex, NodeId nodeId, Properties properties, boolean mutable) {
        super(properties, mutable);
        Assert.isTrue(nodeIndex >= 0, "Node index must not be negative.");
        this.nodeIndex = nodeIndex;
        this.nodeId = nodeId;
    }

    @Override
    public NodeType getType() {
        return nodeId.getNodeType();
    }

    @Override
    public int getIndex() {
        return nodeIndex;
    }

    @Override
    public NodeId getNodeId() {
        return nodeId;
    }

    /**
     * Returns an immutable copy of this node.
     */
    public Node immutableNode() {
        return new NodeImpl(nodeIndex, nodeId, getProperties(), false);
    }

    @Override
    public String toString() {
        return "NodeImpl [nodeIndex=" + nodeIndex + ", nodeId=" + nodeId + ", getProperties()="
            + getProperties() + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((nodeId == null) ? 0 : nodeId.hashCode());
        return result;
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
        NodeImpl other = (NodeImpl) obj;
        if (nodeId == null) {
            if (other.nodeId != null) {
                return false;
            }
        } else if (!nodeId.equals(other.nodeId)) {
            return false;
        }
        return true;
    }

}
