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

import java.util.Collection;

import org.graphit.graph.node.domain.NodeId;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

/**
 * A repository containing methods for getting connected edges and neighbors for
 * a node.
 * 
 * @author jon
 * 
 */
public interface BlueprintsEdgesRepository {

    /**
     * Gets connected edges for a node.
     * 
     * @param nodeId
     *            The node id.
     * @param edgeDirection
     *            The direction of the edges to get.
     * @param edgeLabel
     *            The name of the edge type to get edges for.
     */
    Iterable<Edge> getEdges(NodeId nodeId, Direction edgeDirection, String edgeLabel);

    /**
     * Gets neighbors for a node.
     * 
     * @param nodeId
     *            The node id.
     * @param edgeDirection
     *            The direction of the edges to get neighbors for.
     * @param edgeLabel
     *            The name of the edge type to get neighbors for.
     */
    Iterable<Vertex> getNeighbors(NodeId nodeId, Direction edgeDirection, String edgeLabel);

    /**
     * Gets all edge types in this repo.
     */
    Collection<String> getEdgeTypes();
}