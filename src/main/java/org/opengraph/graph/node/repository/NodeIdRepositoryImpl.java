package org.opengraph.graph.node.repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.mahout.math.map.AbstractObjectIntMap;
import org.apache.mahout.math.map.OpenObjectIntHashMap;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.opengraph.graph.node.domain.NodeId;
import org.opengraph.graph.node.domain.NodePrimitive;
import org.opengraph.graph.node.schema.DynamicNodeTypes;
import org.opengraph.graph.node.schema.NodeType;
import org.opengraph.graph.node.schema.NodeTypeImpl;
import org.opengraph.graph.node.schema.NodeTypes;
import org.opengraph.graph.repository.AbstractGraphRepository;
import org.opengraph.graph.repository.GraphRepositoryFileUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

public class NodeIdRepositoryImpl extends AbstractGraphRepository implements NodeIdRepository {

    private final NodeTypes nodeTypes;
    private final AbstractObjectIntMap<NodeId> nodeMap;
    private final ArrayList<NodeId> nodes;

    public NodeIdRepositoryImpl(NodeTypes nodeTypes) {
        this.nodeTypes = nodeTypes;
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
        String dir = getDirectory();
        if (!StringUtils.hasText(dir)) {
            return;
        }
        File file = new File(FilenameUtils.concat(getDirectory(), getFileName()));
        if (!file.exists()) {
            return;
        }
        try {
            restore(file);
        } catch (IOException e) {
            throw new RuntimeException("Failed to restore nodes.", e);
        }
    }

    @Override
    public synchronized void shutdown() {
        try {
            GraphRepositoryFileUtils.persist(this, getDirectory(), getFileName());
        } catch (IOException e) {
            throw new RuntimeException("Failed to persist nodes.", e);
        }
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
    public synchronized void restore(File in) throws IOException {
        Assert.isTrue(nodes.isEmpty(), "Can not restore a non empty repo.");
        ObjectMapper mapper = new ObjectMapper();
        List<NodePrimitive> primitives =
            mapper.readValue(in, new TypeReference<List<NodePrimitive>>() {
        });
        for (NodePrimitive np : primitives) {
            int index = np.getIndex();
            NodeType nodeType = nodeTypes.valueOf(np.getType());
            NodeId nodeId = new NodeId(nodeType, np.getId());
            insert(index, nodeId);
        }
    }

    @Override
    public String getDirectory() {
        return FilenameUtils.concat(getBaseDirectory(), "nodes/primitives");
    }

    @Override
    protected String getFileName() {
        return "nodes.json";
    }

    public static void main(String[] args) {
        NodeType product = new NodeTypeImpl("Product");
        NodeType user = new NodeTypeImpl("User");
        NodeTypes nodeTypes = new DynamicNodeTypes().add(product).add(user);
        NodeIdRepositoryImpl repo = new NodeIdRepositoryImpl(nodeTypes);
        repo.setBaseDirectory("/tmp/opengraph");
        repo.init();
        repo.shutdown();
    }
}
