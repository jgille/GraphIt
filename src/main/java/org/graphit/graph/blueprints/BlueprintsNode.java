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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.graphit.graph.node.domain.Node;
import org.graphit.graph.node.domain.NodeId;

import com.google.common.collect.Iterables;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Query;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.util.DefaultQuery;

/**
 * A {@link Vertex} implementation.
 *
 * @author jon
 *
 */
class BlueprintsNode extends AbstractElement<BlueprintsNodeId> implements Vertex {

    private final NodeId nodeId;
    private final BlueprintsEdgesRepository edgesRepo;

    /**
     * Constructs a new {@link BlueprintsNode}.
     *
     * @param node
     *            The wrapped node.
     * @param edgesRepo
     *            A repo for getting connected edges and neighbors of this node.
     */
    BlueprintsNode(Node node, BlueprintsEdgesRepository edgesRepo) {
        super(new BlueprintsNodeId(node.getNodeId()), node);
        this.nodeId = node.getNodeId();
        this.edgesRepo = edgesRepo;
    }

    @Override
    public Iterable<Edge> getEdges(Direction direction, String... labels) {
        if (labels.length == 1) {
            return edgesRepo.getEdges(nodeId, direction, labels[0]);
        }

        List<Iterable<Edge>> iterables = new ArrayList<Iterable<Edge>>();
        for (String label : getLabels(labels)) {
            Iterable<Edge> edges = edgesRepo.getEdges(nodeId, direction, label);
            iterables.add(edges);
        }
        return Iterables.concat(iterables);
    }

    @Override
    public Iterable<Vertex> getVertices(Direction direction, String... labels) {
        if (labels.length == 1) {
            return edgesRepo.getNeighbors(nodeId, direction, labels[0]);
        }

        List<Iterable<Vertex>> iterables = new ArrayList<Iterable<Vertex>>();
        for (String label : getLabels(labels)) {
            Iterable<Vertex> neighbors =
                edgesRepo.getNeighbors(nodeId, direction, label);
            iterables.add(neighbors);
        }
        return Iterables.concat(iterables);
    }

    private String[] getLabels(String... labels) {
        if (labels.length == 0) {
            Collection<String> edgeTypes = edgesRepo.getEdgeTypes();
            return edgeTypes.toArray(new String[edgeTypes.size()]);
        }
        return labels;
    }

    @Override
    public Query query() {
        return new DefaultQuery(this);
    }

    @Override
    public String toString() {
        return "BlueprintsNode [getId()=" + getId() + "]";
    }

}
