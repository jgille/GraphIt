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
import java.util.List;

import org.graphit.common.collections.CombinedIterable;
import org.graphit.graph.node.domain.Node;
import org.graphit.graph.node.domain.NodeId;
import org.springframework.util.Assert;

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
class BlueprintsNode extends AbstractElement<NodeId> implements Vertex {

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
        super(node.getNodeId(), node);
        this.edgesRepo = edgesRepo;
    }

    @Override
    public Iterable<Edge> getEdges(Direction direction, String... labels) {
        Assert.isTrue(labels.length > 0, "No edge labels provided.");
        NodeId nodeId = getId();

        if (labels.length == 1) {
            return edgesRepo.getEdges(nodeId, direction, labels[0]);
        }

        List<Iterable<Edge>> iterables = new ArrayList<Iterable<Edge>>();
        for (String label : labels) {
            Iterable<Edge> edges = edgesRepo.getEdges(nodeId, direction, label);
            iterables.add(edges);
        }
        return new CombinedIterable<Edge>(iterables);
    }

    @Override
    public Iterable<Vertex> getVertices(Direction direction, String... labels) {
        Assert.isTrue(labels.length > 0, "No edge labels provided.");
        NodeId nodeId = getId();

        if (labels.length == 1) {
            return edgesRepo.getNeighbors(nodeId, direction, labels[0]);
        }

        List<Iterable<Vertex>> iterables = new ArrayList<Iterable<Vertex>>();
        for (String label : labels) {
            Iterable<Vertex> neighbors =
                edgesRepo.getNeighbors(nodeId, direction, label);
            iterables.add(neighbors);
        }
        return new CombinedIterable<Vertex>(iterables);
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
