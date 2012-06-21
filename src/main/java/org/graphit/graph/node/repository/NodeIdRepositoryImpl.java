package org.graphit.graph.node.repository;

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
import org.graphit.graph.exception.DuplicateKeyException;
import org.graphit.graph.exception.GraphException;
import org.graphit.graph.node.domain.NodeId;
import org.graphit.graph.node.domain.NodePrimitive;
import org.graphit.graph.node.schema.NodeType;
import org.graphit.graph.node.schema.NodeTypes;
import org.graphit.graph.repository.AbstractGraphRepository;
import org.graphit.graph.repository.GraphRepositoryFileUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * {@link NodeIdRepository} implementation storing everything in RAM.
 *
 * @author jon
 *
 */
public class NodeIdRepositoryImpl extends AbstractGraphRepository implements NodeIdRepository {

    private final NodeTypes nodeTypes;
    private final AbstractObjectIntMap<NodeId> nodeMap;
    private final List<NodeId> nodes;

    private boolean isInited = false;
    private boolean isShutdown = false;

    /**
     * Creates a new repo for the provided set of node types.
     */
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
        Assert.isTrue(nodeIndex >= 0, "Illegal node index: " + nodeIndex);
        if (nodeIndex >= nodes.size()) {
            return null;
        }
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
        if (nodes.get(nodeIndex) != null) {
            throw new DuplicateKeyException(nodeIndex);
        }
        nodes.set(nodeIndex, nodeId);
        nodeMap.put(nodeId, nodeIndex);
    }

    @Override
    public synchronized NodeId remove(int nodeIndex) {
        NodeId nodeId;
        Assert.isTrue(nodeIndex >= 0, "Illegal node index: " + nodeIndex);
        if (nodeIndex >= nodes.size()) {
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
    public synchronized void init() {
        try {
        GraphRepositoryFileUtils.restore(this, getDataDirectory(), getFileName());
        } catch (IOException e) {
            throw new GraphException("Failed to restore nodes.", e);
        }
        this.isInited = true;
    }

    @Override
    public synchronized void shutdown() {
        try {
            GraphRepositoryFileUtils.persist(this, getDataDirectory(), getFileName());
        } catch (IOException e) {
            throw new GraphException("Failed to persist nodes.", e);
        }
        this.isShutdown = true;
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
    public String getDataDirectory() {
        String baseDir = getRootDataDirectory();
        if (!StringUtils.hasText(baseDir)) {
            return null;
        }
        return FilenameUtils.concat(getRootDataDirectory(), "nodes/primitives");
    }

    @Override
    protected String getFileName() {
        return "nodes.json";
    }

    synchronized List<NodeId> getNodeIds() {
        return new ArrayList<NodeId>(nodes);
    }

    boolean isInited() {
        return isInited;
    }

    boolean isShutdown() {
        return isShutdown;
    }
}
