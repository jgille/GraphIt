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

import java.util.ArrayList;
import java.util.List;

import org.apache.mahout.math.map.AbstractObjectIntMap;
import org.apache.mahout.math.map.OpenObjectIntHashMap;
import org.graphit.graph.exception.DuplicateKeyException;
import org.graphit.graph.node.domain.NodeId;
import org.springframework.util.Assert;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

/**
 * {@link NodeIdRepository} implementation storing everything in RAM.
 *
 * @author jon
 *
 */
public class NodeIdRepositoryImpl implements NodeIdRepository {

    private final AbstractObjectIntMap<NodeId> nodeMap;
    private final List<NodeId> nodes;

    /**
     * Creates a new repo.
     */
    public NodeIdRepositoryImpl() {
        this.nodeMap = new OpenObjectIntHashMap<NodeId>();
        this.nodes = new ArrayList<NodeId>();
    }

    @Override
    public synchronized int getNodeIndex(NodeId nodeId) {
        Assert.notNull(nodeId);
        if (nodeMap.containsKey(nodeId)) {
            return nodeMap.get(nodeId);
        }
        return -1;
    }

    @Override
    public synchronized NodeId getNodeId(int nodeIndex) {
        Assert.isTrue(nodeIndex >= 0, "Illegal node index: " + nodeIndex);
        if (nodeIndex >= nodes.size()) {
            return null;
        }
        return nodes.get(nodeIndex);
    }

    @Override
    public synchronized int insert(NodeId nodeId) {
        Assert.notNull(nodeId);
        int nodeIndex = nodes.size();
        insert(nodeIndex, nodeId);
        return nodeIndex;
    }

    @Override
    public synchronized void insert(int nodeIndex, NodeId nodeId) {
        Assert.isTrue(nodeIndex >= 0, "Illegal node index.");
        Assert.isTrue(!nodeMap.containsKey(nodeId), "Duplicate node id: " + nodeId);
        for (int i = nodes.size(); i <= nodeIndex; i++) {
            nodes.add(null);
        }
        if (nodes.get(nodeIndex) != null) {
            throw new DuplicateKeyException(nodeIndex);
        }
        nodes.set(nodeIndex, nodeId);
        nodeMap.put(nodeId, nodeIndex);
    }

    @Override
    public synchronized NodeId remove(int nodeIndex) {
        Assert.isTrue(nodeIndex >= 0, "Illegal node index: " + nodeIndex);
        if (nodeIndex >= nodes.size()) {
            return null;
        }
        NodeId nodeId = nodes.get(nodeIndex);
        nodes.set(nodeIndex, null);
        nodeMap.removeKey(nodeId);
        return nodeId;
    }

    @Override
    public synchronized int size() {
        return nodeMap.size();
    }

    @Override
    public synchronized Iterable<NodeId> getNodes() {
        List<NodeId> copy = new ArrayList<NodeId>(nodes);
        Iterable<NodeId> notNull = Iterables.filter(copy, new Predicate<NodeId>() {

            @Override
            public boolean apply(NodeId input) {
                return input != null;
            }
        });
        return notNull;
    }
}
