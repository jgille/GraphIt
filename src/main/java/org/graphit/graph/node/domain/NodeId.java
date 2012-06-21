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

package org.graphit.graph.node.domain;

import org.graphit.graph.node.schema.NodeType;
import org.springframework.util.Assert;

/**
 * Identifies a node in a graph.
 *
 * @author jon
 *
 */
public class NodeId {

    private final NodeType type;
    private final String id;

    /**
     * Created a new instance.
     */
    public NodeId(NodeType type, String id) {
        Assert.notNull(type, "A node type must be provided");
        Assert.notNull(id, "A node id must be provided");
        Assert.isTrue(!id.isEmpty(), "A node id must be provided");
        this.type = type;
        this.id = id;
    }

    /**
     * Gets the node type.
     */
    public NodeType getNodeType() {
        return type;
    }

    /**
     * Gets the node id.
     */
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
