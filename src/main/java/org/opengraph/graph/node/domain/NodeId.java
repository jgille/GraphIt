package org.opengraph.graph.node.domain;

import org.opengraph.graph.node.schema.NodeType;
import org.springframework.util.Assert;

public class NodeId {

    private final NodeType type;
    private final String id;

    public NodeId(NodeType type, String id) {
        Assert.notNull(type, "A node type must be provided");
        Assert.notNull(id, "A node id must be provided");
        Assert.isTrue(!id.isEmpty(), "A node id must be provided");
        this.type = type;
        this.id = id;
    }

    public NodeType getNodeType() {
        return type;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "NodeId [type=" + type + ", id=" + id + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + type.hashCode();
        result = prime * result + id.hashCode();
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
        NodeId other = (NodeId) obj;
        return id.equals(other.id) && type.equals(other.type);
    }

}
