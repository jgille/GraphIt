package org.opengraph.graph.edge.repository;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.opengraph.graph.edge.domain.EdgeId;
import org.opengraph.graph.edge.domain.EdgePrimitive;
import org.opengraph.graph.edge.schema.EdgeType;
import org.opengraph.graph.edge.util.EdgeIndexComparator;
import org.opengraph.graph.edge.util.EdgeWeigher;
import org.opengraph.graph.repository.GraphRepositoryExporter;
import org.springframework.util.Assert;

/**
 * {@link TypedEdgePrimitivesRepository} that stores the edges in a collection of
 * {@link ByteBuffer}s.
 *
 * This class is thread safe.
 *
 * @see AbstractTypedEdgePrimitivesRepository
 *
 * @author jon
 *
 */
public class ByteBufferTypedEdgePrimitivesRepository extends AbstractTypedEdgePrimitivesRepository {

    private static final int DEFAULT_NUM_EDGES_PER_SHARD = 50000;

    private final int numEdgesPerShard;
    private final List<ByteBuffer> shards;
    private final int[] initial;

    private EdgeIndexComparator edgeComparator;

    public ByteBufferTypedEdgePrimitivesRepository(EdgeType edgeType) {
        this(edgeType, DEFAULT_NUM_EDGES_PER_SHARD);
    }

    public ByteBufferTypedEdgePrimitivesRepository(EdgeType edgeType, int numEdgesPerShards) {
        super(edgeType);
        this.numEdgesPerShard = numEdgesPerShards;
        this.shards = new ArrayList<ByteBuffer>();
        this.initial = new int[getShardSize() / 4];
        Arrays.fill(initial, -1);
        this.edgeComparator = getEdgeType().getEdgeComparator(new ByteBufferEdgeWeigher(this));
    }

    public void setEdgeComparator(EdgeIndexComparator edgeComparator) {
        this.edgeComparator = edgeComparator;
    }

    private int getBytesPerEdge() {
        return getEdgeType().isWeighted() ? 12 : 8;
    }

    private int getShardSize() {
        return numEdgesPerShard * getBytesPerEdge();
    }

    private ByteBuffer getShard(int edgeId) {
        int shardIndex = edgeId / numEdgesPerShard;
        synchronized (shards) {
            if (shardIndex >= shards.size()) {
                return null;
            }
            return shards.get(shardIndex);
        }
    }

    private ByteBuffer getOrAddShard(int edgeId) {
        int shardIndex = edgeId / numEdgesPerShard;
        ByteBuffer shard;
        synchronized (shards) {
            if (shardIndex >= shards.size()) {
                shard = ByteBuffer.allocate(getShardSize());
                shard.asIntBuffer().put(initial);
                shards.add(shard);
            } else {
                shard = shards.get(shardIndex);
            }
        }
        return shard;
    }

    private int getIndexInShard(int edgeId) {
        return (edgeId % numEdgesPerShard) * getBytesPerEdge();
    }

    @Override
    public EdgeId addEdge(int startNodeId, int endNodeId) {
        EdgeId edgeId = generateEdgeId();
        addEdge(edgeId, startNodeId, endNodeId);
        return edgeId;
    }

    @Override
    public void addEdge(EdgeId edgeId, int startNodeIndex, int endNodeIndex) {
        upsert(edgeId, startNodeIndex, endNodeIndex, 0);
    }

    @Override
    public EdgeId addWeightedEdge(int startNodeId, int endNodeId, float weight) {
        EdgeId edgeId = generateEdgeId();
        addWeightedEdge(edgeId, startNodeId, endNodeId, weight);
        return edgeId;
    }

    @Override
    public void addWeightedEdge(EdgeId edgeId, int startNodeIndex, int endNodeIndex, float weight) {
        Assert.isTrue(getEdgeType().isWeighted(), "The edges in this repo are unweighted.");
        upsert(edgeId, startNodeIndex, endNodeIndex, weight);
    }

    private EdgeId upsert(EdgeId edgeId, int startNodeIndex, int endNodeIndex, float weight) {
        Assert.isTrue(startNodeIndex >= 0, "Node indexes must not be negative.");
        Assert.isTrue(endNodeIndex >= 0, "Node indexes must not be negative.");
        int id = edgeId.getIndex();
        ByteBuffer shard = getOrAddShard(id);
        int index = getIndexInShard(id);
        boolean newEdge;
        synchronized (shard) {
            int ps = shard.getInt(index);
            int es = shard.getInt(index + 4);
            newEdge = ps < 0;
            if (!newEdge) {
                Assert.isTrue(ps == startNodeIndex);
                Assert.isTrue(es == endNodeIndex);
            }
            shard.putInt(index, startNodeIndex);
            shard.putInt(index + 4, endNodeIndex);
            if (getEdgeType().isWeighted()) {
                shard.putFloat(index + 8, weight);
            }
        }
        EdgePrimitive edge =
            new EdgePrimitive(edgeId, startNodeIndex, endNodeIndex, weight);
        if (newEdge) {
            addEdge(edge);
        }
        return edgeId;
    }

    @Override
    public EdgePrimitive getEdge(EdgeId edgeId) {
        Assert.isTrue(edgeId.getEdgeType().equals(getEdgeType()), "Illegal edge type for: "
            + edgeId);
        int id = edgeId.getIndex();
        ByteBuffer shard = getShard(id);
        if (shard == null) {
            return null;
        }
        int index = getIndexInShard(id);
        int startNodeId, endNodeId;
        float weight;
        synchronized (shard) {
            startNodeId = shard.getInt(index);
            endNodeId = shard.getInt(index + 4);
            weight = getEdgeType().isWeighted() ? shard.getFloat(index + 8) : 0;
        }
        if (startNodeId < 0) {
            return null;
        }
        return new EdgePrimitive(edgeId, startNodeId, endNodeId, weight);
    }

    @Override
    public EdgePrimitive removeEdge(EdgeId edgeId) {
        Assert.isTrue(edgeId.getEdgeType().equals(getEdgeType()), "Illegal edge type for: "
            + edgeId);
        int id = edgeId.getIndex();

        ByteBuffer shard = getShard(id);
        if (shard == null) {
            return null;
        }

        int index = getIndexInShard(id);
        int startNodeId, endNodeId;
        float weight;
        synchronized (shard) {
            startNodeId = shard.getInt(index);
            endNodeId = shard.getInt(index + 4);
            weight = getEdgeType().isWeighted() ? shard.getFloat(index + 8) : 0;
            // Delete by setting start and end node id to -1
            shard.putInt(index, -1).putInt(index + 4, -1);
        }
        if (startNodeId < 0) {
            return null;
        }

        EdgePrimitive edge =
            new EdgePrimitive(edgeId, startNodeId, endNodeId, weight);
        removeEdge(edge);
        return edge;
    }

    @Override
    public void setEdgeWeight(EdgeId edgeId, float weight) {
        EdgePrimitive edge = getEdge(edgeId);
        Assert.notNull(edge);
        upsert(edgeId, edge.getStartNodeId(), edge.getEndNodeId(), weight);
        reindex(new EdgePrimitive(edgeId, edge.getStartNodeId(), edge.getEndNodeId(), weight));
    }

    @Override
    public void removeNode(int nodeIndex) {
        removeOutgoingEdges(nodeIndex);
        removeIncomingEdges(nodeIndex);
    }

    @Override
    public float getEdgeWeight(int edgeId) {
        if (!getEdgeType().isWeighted()) {
            return 0;
        }
        ByteBuffer shard = getShard(edgeId);
        int index = getIndexInShard(edgeId);
        float weight;
        synchronized (shard) {
            weight = shard.getFloat(index + 8);
        }
        return weight;
    }

    @Override
    protected EdgeIndexComparator getEdgeComparator() {
        return edgeComparator;
    }

    @Override
    public String toString() {
        return "ShardedByteBufferInternalEdgesRepository [edgeType=" + getEdgeType() + "]";
    }

    private static class ByteBufferEdgeWeigher implements EdgeWeigher {

        private final ByteBufferTypedEdgePrimitivesRepository repo;

        private ByteBufferEdgeWeigher(ByteBufferTypedEdgePrimitivesRepository repo) {
            this.repo = repo;
        }

        @Override
        public float getEdgeWeight(int edgeId) {
            return repo.getEdgeWeight(edgeId);
        }
    }

    @Override
    public void init() {
        // TODO Auto-generated method stub

    }
    @Override
    public void shutdown() {
        try {
            new GraphRepositoryExporter(this).export(getDirectory(), getFileName());
        } catch (IOException e) {
            throw new RuntimeException("Failed to export nodes.", e);
        }
    }

    @Override
    public synchronized void dump(File out) throws IOException {
        Writer writer = new FileWriter(out);
        try {
            for (ByteBuffer buffer : shards) {
                byte[] arr = buffer.array();
                IOUtils.write(arr, writer, "UTF8");
            }
        } finally {
            writer.close();
        }
    }

    @Override
    public void restore(File in) throws IOException {
        // TODO Auto-generated method stub

    }

    @Override
    protected String getDirectory() {
        return FilenameUtils.concat(getBaseDirectory(), "edges");
    }

    @Override
    protected String getFileName() {
        return String.format("%s.bin", getEdgeType().name());
    }

}
