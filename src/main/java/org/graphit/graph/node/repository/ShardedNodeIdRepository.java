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

package org.graphit.graph.node.repository;

import org.graphit.graph.domain.MappedList;
import org.graphit.graph.domain.ShardedMappedList;
import org.graphit.graph.node.domain.NodeId;
import org.springframework.util.Assert;

import com.google.common.collect.Iterables;

/**
 * {@link NodeIdRepository} implementation storing everything in RAM.
 *
 * @author jon
 *
 */
public class ShardedNodeIdRepository implements NodeIdRepository {

    private final MappedList<NodeId> nodes;

    /**
     * Creates a new repo.
     */
    public ShardedNodeIdRepository() {
        // TODO: Find a suitable default concurrency level
        this(Runtime.getRuntime().availableProcessors() * 2);
    }
    /**
     * Creates a new repo.
     */
    public ShardedNodeIdRepository(int concurrencyLevel) {
        this.nodes = new ShardedMappedList<NodeId>(concurrencyLevel);
    }

    @Override
    public int getNodeIndex(NodeId nodeId) {
        Assert.notNull(nodeId);
        return nodes.indexOf(nodeId);
    }

    @Override
    public NodeId getNodeId(int nodeIndex) {
        Assert.isTrue(nodeIndex >= 0, "Illegal node index");
        return nodes.get(nodeIndex);
    }

    @Override
    public int insert(NodeId nodeId) {
        Assert.notNull(nodeId);
        return nodes.add(nodeId);
    }

    @Override
    public void insert(int nodeIndex, NodeId nodeId) {
        nodes.set(nodeIndex, nodeId);
    }

    @Override
    public NodeId remove(int nodeIndex) {
        return nodes.remove(nodeIndex);
    }

    @Override
    public int size() {
        // Very inefficient. TODO: Fix this.
        return Iterables.size(getNodes());
    }

    @Override
    public Iterable<NodeId> getNodes() {
        return nodes.iterable();
    }
}
