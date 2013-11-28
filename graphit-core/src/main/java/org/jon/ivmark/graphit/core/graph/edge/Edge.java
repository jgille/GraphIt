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

package org.jon.ivmark.graphit.core.graph.edge;

import org.jon.ivmark.graphit.core.graph.entity.GraphEntity;
import org.jon.ivmark.graphit.core.graph.node.Node;
import org.jon.ivmark.graphit.core.graph.node.NodeId;

/**
 * An edge in a graph.
 * 
 * @author jon
 * 
 */
public interface Edge extends GraphEntity<EdgeType> {

    /**
     * Gets the id for this edge.
     */
    EdgeId getEdgeId();

    /**
     * Gets the start node.
     */
    Node getStartNode();

    /**
     * Gets the end node.
     */
    Node getEndNode();

    /**
     * Gets the opposite node from the provided node. Throws an exception if the
     * provided node is neither start or end node of this edge.
     */
    Node getOppositeNode(NodeId nodeId);

    /**
     * Gets the edge weight.
     */
    float getWeight();

}
