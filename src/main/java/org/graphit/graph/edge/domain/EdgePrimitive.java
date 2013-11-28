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

package org.graphit.graph.edge.domain;

import org.graphit.graph.edge.schema.EdgeType;

/**
 * The internal representation of an edge.
 * 
 * @author jon
 * 
 */
public class EdgePrimitive {

    private final EdgeId edgeId;
    private final int startNodeIndex;
    private final int endNodeIndex;
    private final float weight;

    /**
     * Constructs a new primitive.
     */
    public EdgePrimitive(EdgeId edgeId, int startNodeIndex, int endNodeIndex, float weight) {
        this.edgeId = edgeId;
        this.startNodeIndex = startNodeIndex;
        this.endNodeIndex = endNodeIndex;
        this.weight = weight;
    }

    /**
     * Gets the edge index.
     */
    public int getIndex() {
        return edgeId.getIndex();
    }

    /**
     * Gets the edge type.
     */
    public EdgeType getEdgeType() {
        return edgeId.getEdgeType();
    }

    /**
     * Gets the edge id.
     */
    public EdgeId getEdgeId() {
        return edgeId;
    }

    /**
     * Gets index of the start node.
     */
    public int getStartNodeIndex() {
        return startNodeIndex;
    }

    /**
     * Gets index of the end node.
     */
    public int getEndNodeIndex() {
        return endNodeIndex;
    }

    /**
     * Gets the edge weight.
     */
    public float getWeight() {
        return weight;
    }

    /**
     * Returns a reversed copy of the edge, i.e. A -> B will become B -> A.
     */
    public EdgePrimitive reverse() {
        return new EdgePrimitive(edgeId, endNodeIndex, startNodeIndex, weight);
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
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        EdgePrimitive other = (EdgePrimitive) obj;
        return edgeId.equals(other.edgeId);
    }

    @Override
    public String toString() {
        return "InternalEdge [edgeId=" + edgeId + ", startNodeId=" + startNodeIndex
            + ", endNodeId="
            + endNodeIndex + ", weight=" + weight + "]";
    }

}
