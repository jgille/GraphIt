package org.graphit.graph.edge.repository;

import java.util.ArrayList;
import java.util.List;

import org.apache.mahout.math.map.AbstractIntObjectMap;
import org.apache.mahout.math.map.OpenIntObjectHashMap;
import org.graphit.graph.edge.domain.EdgeVector;

/**
 * An {@link EdgeVectorRepository} that is split into multiple shards for better
 * concurrency.
 *
 */
public class ShardedEdgeVectorRepository implements EdgeVectorRepository {

    private final int nofShards;
    private final List<AbstractIntObjectMap<EdgeVector>> outgoingShards;
    private final List<AbstractIntObjectMap<EdgeVector>> incomingShards;

    /**
     * Creates a new repo with the provided number of shards.
     */
    public ShardedEdgeVectorRepository(int nofShards) {
        this.nofShards = nofShards;
        this.outgoingShards = new ArrayList<AbstractIntObjectMap<EdgeVector>>(nofShards);
        this.incomingShards = new ArrayList<AbstractIntObjectMap<EdgeVector>>(nofShards);

        for (int i = 0; i < nofShards; i++) {
            this.outgoingShards.add(new OpenIntObjectHashMap<EdgeVector>());
            this.incomingShards.add(new OpenIntObjectHashMap<EdgeVector>());
        }
    }

    private int getShardIndex(int nodeIndex) {
        return nodeIndex % nofShards;
    }

    private AbstractIntObjectMap<EdgeVector> getOutgoingShard(int nodeIndex) {
        int shardIndex = getShardIndex(nodeIndex);
        synchronized (outgoingShards) {
            return outgoingShards.get(shardIndex);
        }
    }

    private AbstractIntObjectMap<EdgeVector> getIncomingShard(int nodeIndex) {
        int shardIndex = getShardIndex(nodeIndex);
        synchronized (incomingShards) {
            return incomingShards.get(shardIndex);
        }
    }

    private EdgeVector get(AbstractIntObjectMap<EdgeVector> map, int nodeIndex) {
        synchronized (map) {
            return map.get(nodeIndex);
        }
    }

    private void put(AbstractIntObjectMap<EdgeVector> map, int nodeIndex, EdgeVector edges) {
        synchronized (map) {
            map.put(nodeIndex, edges);
        }
    }

    private EdgeVector remove(AbstractIntObjectMap<EdgeVector> map, int nodeIndex) {
        synchronized (map) {
            EdgeVector res = map.get(nodeIndex);
            map.removeKey(nodeIndex);
            return res;
        }
    }

    @Override
    public EdgeVector getOutgoingEdges(int startNodeIndex) {
        return get(getOutgoingShard(startNodeIndex), startNodeIndex);
    }

    @Override
    public EdgeVector getIncomingEdges(int endNodeIndex) {
        return get(getIncomingShard(endNodeIndex), endNodeIndex);
    }

    @Override
    public void setOutgoingEdges(int endNodeIndex, EdgeVector edges) {
        int startNodeIndex = edges.getRootNode();
        put(getOutgoingShard(startNodeIndex), startNodeIndex, edges);
    }

    @Override
    public void setIncomingEdges(int endNodeIndex, EdgeVector edges) {
        put(getIncomingShard(endNodeIndex), endNodeIndex, edges);
    }

    @Override
    public EdgeVector removeOutgoingEdges(int startNodeIndex) {
        return remove(getOutgoingShard(startNodeIndex), startNodeIndex);
    }

    @Override
    public EdgeVector removeIncomingEdges(int endNodeIndex) {
        return remove(getIncomingShard(endNodeIndex), endNodeIndex);
    }

}
