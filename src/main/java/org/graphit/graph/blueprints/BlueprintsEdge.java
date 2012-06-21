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

package org.graphit.graph.blueprints;

import org.graphit.graph.edge.domain.EdgeId;
import org.graphit.properties.domain.Properties;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

/**
 * An {@link Edge} implmentation.
 *
 * @author jon
 *
 */
public class BlueprintsEdge extends AbstractElement<EdgeId> implements Edge {

    private final Vertex startNode;
    private final Vertex endNode;

    /**
     * Constructs an edge.
     *
     * @param edgeId
     *            The id of this edge.
     * @param startNode
     *            The edge start node.
     * @param endNode
     *            The edge end node.
     * @param properties
     *            The edge properties.
     */
    BlueprintsEdge(EdgeId edgeId, Vertex startNode, Vertex endNode,
                   Properties properties) {
        super(edgeId, properties);
        this.startNode = startNode;
        this.endNode = endNode;
    }

    @Override
    public Vertex getVertex(Direction direction) {
        switch (direction) {
        case OUT:
            return startNode;
        case IN:
            return endNode;
        default:
            throw new IllegalArgumentException("Illegal direction: " + direction);
        }
    }

    @Override
    public String getLabel() {
        return getId().getEdgeType().name();
    }

    @Override
    public String toString() {
        return "BlueprintsEdge [startNode=" + startNode + ", endNode=" + endNode + ", getLabel()="
            + getLabel() + "]";
    }

}
