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

import org.springframework.util.Assert;

/**
 * Describes a node, minus it's {@link Properties}. Primarily used to dump and
 * restore nodes to/from disk.
 *
 * @author jon
 *
 */
public class NodePrimitive {

    private int index = -1;
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
        validate();
        final int prime = 31;
        int result = 1;
        result = prime * result + id.hashCode();
        result = prime * result + index;
        result = prime * result + type.hashCode();
        return result;
    }

    private void validate() {
        Assert.notNull(id);
        Assert.notNull(type);
        Assert.isTrue(index >= 0);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        validate();
        NodePrimitive other = (NodePrimitive) obj;
        other.validate();
        return other.getIndex() == index && other.getId().equals(id)
            && other.getType().equals(type);
    }

}
