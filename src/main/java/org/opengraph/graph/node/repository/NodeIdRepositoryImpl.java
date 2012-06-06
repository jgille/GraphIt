package org.opengraph.graph.node.repository;

import java.util.ArrayList;
import org.apache.mahout.math.map.AbstractObjectIntMap;
import org.apache.mahout.math.map.OpenObjectIntHashMap;
import org.opengraph.graph.node.domain.NodeId;
import org.springframework.util.Assert;

public class NodeIdRepositoryImpl implements NodeIdRepository {

    private final AbstractObjectIntMap<NodeId> nodeMap;
    private final ArrayList<NodeId> nodes;

    public NodeIdRepositoryImpl() {
        this.nodeMap = new OpenObjectIntHashMap<NodeId>();
        this.nodes = new ArrayList<NodeId>();
    }

    @Override
    public synchronized int getNodeIndex(NodeId nodeId) {
        if (nodeMap.containsKey(nodeId)) {
            return nodeMap.get(nodeId);
        }
        return -1;
    }

    @Override
    public synchronized NodeId getNodeId(int nodeIndex) {
        Assert.isTrue(nodeIndex >= 0 && nodeIndex < nodes.size(), "Illegal node index: "
            + nodeIndex);
        return nodes.get(nodeIndex);
    }

    @Override
    public synchronized int insert(NodeId nodeId) {
        int nodeIndex = nodes.size();
        insert(nodeIndex, nodeId);
        return nodeIndex;
    }

    @Override
    public synchronized void insert(int nodeIndex, NodeId nodeId) {
        Assert.isTrue(nodeIndex >= 0, "Illegal node index.");
        Assert.isTrue(!nodeMap.containsKey(nodeId), "Duplicate node id: " + nodeId);
        while (nodes.size() <= nodeIndex) {
            nodes.add(null);
        }
        Assert.isNull(nodes.get(nodeIndex), "Duplicate node index: " + nodeIndex);
        nodes.set(nodeIndex, nodeId);
        nodeMap.put(nodeId, nodeIndex);
    }

    @Override
    public synchronized NodeId remove(int nodeIndex) {
        NodeId nodeId;
        if (nodeIndex < 0 && nodeIndex >= nodes.size()) {
            return null;
        }
        nodeId = nodes.get(nodeIndex);
        nodes.set(nodeIndex, null);
        nodeMap.removeKey(nodeId);
        return nodeId;
    }

    @Override
    public String toString() {
        return "NodeIndexImpl [size: " + nodes.size() + "]";
    }
}
