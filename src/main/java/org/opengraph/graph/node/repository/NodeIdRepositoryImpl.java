package org.opengraph.graph.node.repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FilenameUtils;
import org.apache.mahout.math.map.AbstractObjectIntMap;
import org.apache.mahout.math.map.OpenObjectIntHashMap;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.opengraph.graph.node.domain.NodeId;
import org.opengraph.graph.node.domain.NodePrimitive;
import org.opengraph.graph.node.schema.NodeType;
import org.opengraph.graph.repository.AbstractGraphRepository;
import org.opengraph.graph.repository.GraphRepositoryExporter;
import org.springframework.util.Assert;

public class NodeIdRepositoryImpl extends AbstractGraphRepository implements NodeIdRepository {

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

    @Override
    public void init() {
        // TODO Auto-generated method stub

    }

    @Override
    public synchronized void dump(File file) throws IOException {
        JsonFactory jsonFactory = new JsonFactory(new ObjectMapper());
        JsonGenerator generator = jsonFactory.createJsonGenerator(file, JsonEncoding.UTF8);
        try {
            generator.writeStartArray();
            for (NodeId nodeId : nodes) {
                int index = nodeMap.get(nodeId);
                NodePrimitive primitive = new NodePrimitive(index, nodeId);
                generator.writeObject(primitive);
            }
            generator.writeEndArray();
        } finally {
            generator.close();
        }
    }

    @Override
    public void restore(File in) throws IOException {
        // TODO Auto-generated method stub

    }

    @Override
    public String getDirectory() {
        return FilenameUtils.concat(getBaseDirectory(), "nodes");
    }

    @Override
    protected String getFileName() {
        return "nodes.json";
    }

    @Override
    public synchronized void shutdown() {
        try {
            new GraphRepositoryExporter(this).export(getDirectory(), getFileName());
        } catch (IOException e) {
            throw new RuntimeException("Failed to export nodes.", e);
        }
    }

    public static void main(String[] args) {
        NodeIdRepositoryImpl repo = new NodeIdRepositoryImpl();
        repo.setBaseDirectory("/tmp/opengraph");
        repo.init();

        NodeType product = new NodeType() {

            @Override
            public String name() {
                return "PRODUCT";
            }
        };
        NodeType user = new NodeType() {

            @Override
            public String name() {
                return "USER";
            }
        };

        repo.insert(new NodeId(product, "p1"));
        repo.insert(new NodeId(product, "p2"));
        repo.insert(new NodeId(product, "p3"));

        repo.insert(new NodeId(user, "u1"));
        repo.insert(new NodeId(user, "u2"));

        repo.shutdown();
    }
}
