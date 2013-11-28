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

package org.jon.ivmark.graphit.core.graph.node.domain;

import com.google.common.base.Preconditions;
import org.jon.ivmark.graphit.core.graph.node.schema.NodeType;
import org.jon.ivmark.graphit.core.properties.domain.Properties;
import org.jon.ivmark.graphit.core.properties.domain.PropertiesProxy;

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
        Preconditions.checkArgument(nodeIndex >= 0, "Node index must not be negative.");
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
