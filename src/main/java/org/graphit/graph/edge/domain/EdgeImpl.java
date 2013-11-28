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

import com.google.common.base.Preconditions;
import org.graphit.graph.edge.schema.EdgeType;
import org.graphit.graph.node.domain.Node;
import org.graphit.graph.node.domain.NodeId;
import org.graphit.properties.domain.Properties;
import org.graphit.properties.domain.PropertiesProxy;

/**
 * An edge in a graph.
 *
 * Note: This class is not thread safe.
 *
 * @author jon
 */
public class EdgeImpl extends PropertiesProxy implements Edge {

    private final EdgeId edgeId;
    private Node startNode;
    private Node endNode;
    private float weight;

    /**
     * Creates an edge.
     *
     * @param index
     *            The edge index.
     * @param edgeType
     *            The edge type.
     * @param properties
     *            The edge properties.
     */
    public EdgeImpl(int index, EdgeType edgeType, Properties properties) {
        super(properties, true);
        this.edgeId = new EdgeId(edgeType, index);
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

    /**
     * Sets the edge start node.
     */
    public EdgeImpl setStartNode(Node startNode) {
        Preconditions.checkNotNull(startNode, "Null start node");
        this.startNode = startNode;
        return this;
    }

    /**
     * Sets the edge end node.
     */
    public EdgeImpl setEndNode(Node endNode) {
        Preconditions.checkNotNull(endNode, "Null end node");
        this.endNode = endNode;
        return this;
    }

    /**
     * Sets the edge weight.
     */
    public EdgeImpl setWeight(float weight) {
        this.weight = weight;
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
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        EdgeImpl other = (EdgeImpl) obj;
        return edgeId.equals(other.edgeId);
    }

    @Override
    public String toString() {
        return "EdgeImpl [edgeId=" + edgeId + ", startNode=" + startNode + ", endNode=" + endNode
            + ", weight=" + weight + ", properties=" + asPropertyMap() + "]";
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
