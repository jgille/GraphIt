package org.opengraph.graph.edge.repository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.mahout.math.function.IntProcedure;
import org.apache.mahout.math.list.IntArrayList;
import org.opengraph.graph.edge.domain.EdgeId;
import org.opengraph.graph.edge.domain.EdgePrimitive;
import org.opengraph.graph.edge.schema.EdgeType;
import org.opengraph.graph.edge.util.EdgeIndexComparator;
import org.opengraph.graph.edge.util.EdgeWeigher;
import org.opengraph.graph.repository.GraphRepositoryFileUtils;
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

    /**
     * The number of bytes used to store an entity, e.g. a start/end node index
     * (int) or an edge weight (float).
     */
    private static final int BYTES_PER_ENTITY = 4;

    private final int numEdgesPerShard;
    private final List<ByteBuffer> shards;
    private final int[] initial;

    private EdgeIndexComparator edgeComparator;

    /**
     * Creates a new repo for the provided edge type with a default shard size.
     */
    public ByteBufferTypedEdgePrimitivesRepository(EdgeType edgeType) {
        this(edgeType, DEFAULT_NUM_EDGES_PER_SHARD);
    }

    /**
     * Creates a new repo for the provided edge type and shard size.
     */
    public ByteBufferTypedEdgePrimitivesRepository(EdgeType edgeType, int numEdgesPerShards) {
        super(edgeType);
        this.numEdgesPerShard = numEdgesPerShards;
        this.shards = new ArrayList<ByteBuffer>();
        this.initial = new int[getShardSize() / BYTES_PER_ENTITY];
        Arrays.fill(initial, -1);
        this.edgeComparator = getEdgeType().getEdgeComparator(new ByteBufferEdgeWeigher(this));
    }

    /**
     * Sets the {@link EdgeIndexComparator} to use to keep {@link EdgeVector}s
     * sorted.
     */
    public void setEdgeComparator(EdgeIndexComparator edgeComparator) {
        this.edgeComparator = edgeComparator;
    }

    private int getBytesPerEdge() {
        return getEdgeType().isWeighted() ? BYTES_PER_ENTITY * 3 : BYTES_PER_ENTITY * 2;
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

    private int getOffsetInShard(int edgeId) {
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
        int index = getOffsetInShard(id);
        boolean newEdge;
        synchronized (shard) {
            int ps = shard.getInt(index);
            int es = shard.getInt(index + BYTES_PER_ENTITY);
            newEdge = ps < 0;
            if (!newEdge) {
                Assert.isTrue(ps == startNodeIndex);
                Assert.isTrue(es == endNodeIndex);
            }
            shard.putInt(index, startNodeIndex);
            shard.putInt(index + 4, endNodeIndex);
            if (getEdgeType().isWeighted()) {
                shard.putFloat(index + BYTES_PER_ENTITY * 2, weight);
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
        int offset = getOffsetInShard(id);
        synchronized (shard) {
            return getEdge(edgeId.getIndex(), offset, shard);
        }
    }

    private EdgePrimitive getEdge(int index, int offset, ByteBuffer shard) {
        int startNodeId, endNodeId;
        float weight;
        if (offset > shard.capacity() - getBytesPerEdge()) {
            throw new IllegalStateException("Index out of bounds: " + offset + " >= "
                + shard.capacity());
        }
        startNodeId = shard.getInt(offset);
        endNodeId = shard.getInt(offset + BYTES_PER_ENTITY);
        weight = getEdgeType().isWeighted() ? shard.getFloat(offset + BYTES_PER_ENTITY * 2) : 0;
        if (startNodeId < 0) {
            return null;
        }
        return new EdgePrimitive(new EdgeId(getEdgeType(), index), startNodeId, endNodeId, weight);
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

        int index = getOffsetInShard(id);
        int startNodeId, endNodeId;
        float weight;
        synchronized (shard) {
            startNodeId = shard.getInt(index);
            endNodeId = shard.getInt(index + BYTES_PER_ENTITY);
            weight = getEdgeType().isWeighted() ? shard.getFloat(index + BYTES_PER_ENTITY * 2) : 0;
            // Delete by setting start and end node id to -1
            shard.putInt(index, -1).putInt(index + BYTES_PER_ENTITY, -1);
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
        upsert(edgeId, edge.getStartNodeIndex(), edge.getEndNodeIndex(), weight);
        reindex(new EdgePrimitive(edgeId, edge.getStartNodeIndex(), edge.getEndNodeIndex(), weight));
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
        int index = getOffsetInShard(edgeId);
        float weight;
        synchronized (shard) {
            weight = shard.getFloat(index + BYTES_PER_ENTITY * 2);
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

    @Override
    public void init() {
        GraphRepositoryFileUtils.restore(this, getDirectory(), getFileName());
    }

    @Override
    public void shutdown() {
        try {
            GraphRepositoryFileUtils.persist(this, getDirectory(), getFileName());
        } catch (IOException e) {
            throw new RuntimeException("Failed to export nodes.", e);
        }
    }

    @Override
    public synchronized void dump(File out) throws IOException {
        OutputStream os = new FileOutputStream(out);
        try {
            for (ByteBuffer buffer : shards) {
                byte[] arr = buffer.array();
                os.write(arr);
            }
        } finally {
            os.close();
        }
    }

    private byte[] read(File in) throws IOException {
        return FileUtils.readFileToByteArray(in);
    }

    @Override
    public synchronized void restore(File in) throws IOException {
        Assert.isTrue(shards.isEmpty(), "Can not restore a non empty repo.");
        byte[] data = read(in);
        ByteBuffer buffer = ByteBuffer.wrap(data);
        int index = 0;

        IntArrayList nullEdges = new IntArrayList();
        int maxNonNullIndex = 0;
        int max = data.length / getBytesPerEdge();
        EdgeType type = getEdgeType();
        boolean isWeighted = type.isWeighted();

        while (index < max) {
            getOrAddShard(index);
            EdgePrimitive edge = getEdge(index, index * getBytesPerEdge(), buffer);
            if (edge == null) {
                nullEdges.add(index);
                edge = new EdgePrimitive(new EdgeId(getEdgeType(), index), -1, -1, -1);
            } else {
                maxNonNullIndex = index;
            }
            ByteBuffer shard = getOrAddShard(index);
            int shardIndex = getOffsetInShard(index);
            shard.putInt(shardIndex, edge.getStartNodeIndex());
            shard.putInt(shardIndex + BYTES_PER_ENTITY, edge.getEndNodeIndex());
            if (isWeighted) {
                shard.putFloat(shardIndex + BYTES_PER_ENTITY * 2, edge.getWeight());
            }
            index++;
        }

        maxId.set(maxNonNullIndex);
        nullEdges.forEach(new IntProcedure() {

            @Override
            public boolean apply(int edgeIndex) {
                if (edgeIndex <= maxId.get()) {
                    removedEdges.add(edgeIndex);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected String getDirectory() {
        return FilenameUtils.concat(getBaseDirectory(), "edges/primitives");
    }

    @Override
    protected String getFileName() {
        return String.format("%s.edges", getEdgeType().name());
    }

    private static final class ByteBufferEdgeWeigher implements EdgeWeigher {

        private final ByteBufferTypedEdgePrimitivesRepository repo;

        private ByteBufferEdgeWeigher(ByteBufferTypedEdgePrimitivesRepository repo) {
            this.repo = repo;
        }

        @Override
        public float getEdgeWeight(int edgeId) {
            return repo.getEdgeWeight(edgeId);
        }
    }

}
