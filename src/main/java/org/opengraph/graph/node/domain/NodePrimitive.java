package org.opengraph.graph.node.domain;

import org.opengraph.properties.domain.Properties;

/**
 * Describes a node, minus it's {@link Properties}. Primarily used to dump and
 * restore nodes to/from disk.
 *
 * @author jon
 *
 */
public class NodePrimitive {

    private int index;
    private String type;
    private String id;

    /**
     * Creates a new node primitive.
     */
    public NodePrimitive(int index, NodeId nodeId) {
        this.index = index;
        this.type = nodeId.getNodeType().name();
        this.id = nodeId.getId();
    }

    /**
     * Creates a new node primitive. Used when deserializing nodes using
     * jackson. Note that index, type and id MUST be set for a primitive to be
     * valid.
     */
    public NodePrimitive() {
    }

    /**
     * Gets the index of this node primitive.
     */
    public int getIndex() {
        return index;
    }

    /**
     * Sets the index of this node primitive.
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * Gets the name of the type for this node primitive.
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the name of the type for this node primitive.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets the id of this node primitive.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the id of this node primitive.
     */

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "NodePrimitive [index=" + index + ", type=" + type + ", id=" + id + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + index;
        result = prime * result + ((type == null) ? 0 : type.hashCode());
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
        NodePrimitive other = (NodePrimitive) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        if (index != other.index) {
            return false;
        }
        if (type == null) {
            if (other.type != null) {
                return false;
            }
        } else if (!type.equals(other.type)) {
            return false;
        }
        return true;
    }

}
