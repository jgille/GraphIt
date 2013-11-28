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

package org.jon.ivmark.graphit.core.graph.edge.repository;

import org.apache.mahout.math.list.IntArrayList;
import org.jon.ivmark.graphit.core.ConcurrencyConstants;
import org.jon.ivmark.graphit.core.graph.edge.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Base implementation of an {@link TypedEdgePrimitivesRepository}.
 *
 * This repo stores mappings from node id to outgoing/incoming edges using
 * mahout primitive collections.
 *
 * This class also keeps a list of removed edge ids, used in order to re-use
 * these ids to avoid fragmentation.
 *
 * This class is thread safe.
 *
 * @author jon
 *
 */
public abstract class AbstractTypedEdgePrimitivesRepository implements
    TypedEdgePrimitivesRepository {

    private final AtomicInteger maxId;
    private final IntArrayList removedEdges;
    private final EdgeVectorRepository edgeVectorIndex;
    private final EdgeType edgeType;
    private EdgeIndexComparator edgeComparator;

    private final List<ReentrantLock> locks;

    protected AbstractTypedEdgePrimitivesRepository(EdgeType edgeType) {
        this.edgeType = edgeType;
        this.removedEdges = new IntArrayList();
        this.maxId = new AtomicInteger(-1);
        int nofLocks = ConcurrencyConstants.DEFAULT_CONCURRENCY_LEVEL;
        List<ReentrantLock> lockList = new ArrayList<ReentrantLock>(nofLocks);
        for (int i = 0; i < nofLocks; i++) {
            lockList.add(new ReentrantLock());
        }
        this.locks = Collections.unmodifiableList(lockList);
        this.edgeVectorIndex = new ShardedEdgeVectorRepository(nofLocks);
        this.edgeComparator = edgeType.getSortOrder().getEdgeComparator(this);
    }

    /**
     * Sets the {@link EdgeIndexComparator} to use to keep {@link EdgeVector}s
     * sorted.
     */
    public void setEdgeComparator(EdgeIndexComparator edgeComparator) {
        this.edgeComparator = edgeComparator;
    }

    /**
     * Generated a valid id for an edge that is to be added.
     */
    protected EdgeId generateEdgeId() {
        int id = -1;
        synchronized (removedEdges) {
            if (!removedEdges.isEmpty()) {
                int index = removedEdges.size() - 1;
                id = removedEdges.get(index);
                removedEdges.remove(index);
            }
        }
        if (id < 0) {
            id = maxId.incrementAndGet();
        }
        return new EdgeId(edgeType, id);
    }

    private void addOutgoingEdge(EdgePrimitive edge) {
        int startNodeId = edge.getStartNodeIndex();
        ReentrantLock lock = lockNode(startNodeId);
        try {
            EdgeVector outgoingEdges = findOutgoingEdges(startNodeId);
            if (outgoingEdges == null) {
                EdgeVectorImpl edgeVector = new EdgeVectorImpl(startNodeId, edgeType);
                edgeVector.setEdgeDirection(EdgeDirection.OUTGOING);
                edgeVector.setEdgeComparator(edgeComparator);
                outgoingEdges = edgeVector;
            }
            int edgeId = edge.getIndex();
            EdgeVector newOutgoingEdges = outgoingEdges.add(edgeId);
            setOutgoingEdges(startNodeId, newOutgoingEdges);
        } finally {
            lock.unlock();
        }
    }

    private void removeOutgoingEdge(EdgePrimitive edge) {
        int startNodeId = edge.getStartNodeIndex();
        ReentrantLock lock = lockNode(startNodeId);
        try {

            EdgeVector outgoingEdges = findOutgoingEdges(startNodeId);
            if (outgoingEdges != null) {
                int edgeId = edge.getIndex();
                EdgeVector newOutgoingEdges = outgoingEdges.remove(edgeId);
                setOutgoingEdges(startNodeId, newOutgoingEdges);
            }
        } finally {
            lock.unlock();
        }
    }

    private void addIncomingEdge(EdgePrimitive edge) {
        int endNodeId = edge.getEndNodeIndex();
        ReentrantLock lock = lockNode(endNodeId);
        try {

            EdgeVector incomingEdges = findIncomingEdges(endNodeId);
            if (incomingEdges == null) {
                EdgeVectorImpl edgeVector = new EdgeVectorImpl(endNodeId, edgeType);
                edgeVector.setEdgeDirection(EdgeDirection.INCOMING);
                edgeVector.setEdgeComparator(edgeComparator);
                incomingEdges = edgeVector;
            }
            int edgeId = edge.getIndex();
            EdgeVector newIncomingEdges = incomingEdges.add(edgeId);
            setIncomingEdges(endNodeId, newIncomingEdges);
        } finally {
            lock.unlock();
        }
    }

    private void removeIncomingEdge(EdgePrimitive edge) {
        int endNodeId = edge.getEndNodeIndex();
        ReentrantLock lock = lockNode(endNodeId);
        try {

            EdgeVector incomingEdges = findIncomingEdges(endNodeId);
            if (incomingEdges != null) {
                int edgeId = edge.getIndex();
                EdgeVector newIncomingEdges = incomingEdges.remove(edgeId);
                setIncomingEdges(endNodeId, newIncomingEdges);
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Adds an edge, adding it to the {@link EdgeVector} of the start and end
     * node.
     */
    protected void insert(EdgePrimitive edge) {
        synchronized (maxId) {
            maxId.set(Math.max(maxId.get(), edge.getIndex()));
        }
        // Add the undirected edge as an outgoing edge from both the start
        // and end node
        addOutgoingEdge(edge);
        addIncomingEdge(edge);
    }

    /**
     * Removes an edge, making the id eligable for reuse.
     */
    protected void delete(EdgePrimitive edge) {
        int edgeId = edge.getIndex();
        removeOutgoingEdge(edge);
        removeIncomingEdge(edge);
        synchronized (removedEdges) {
            removedEdges.add(edgeId);
        }
    }

    /**
     * Reindexes (re-sorts) the {@link EdgeVector} of the start and end node
     * after an edge has been modified.
     */
    protected void reindex(EdgePrimitive edge) {
        reindexOutgoing(edge);
        reindexIncoming(edge);
    }

    private void reindexOutgoing(EdgePrimitive edge) {
        int startNodeId = edge.getStartNodeIndex();
        ReentrantLock lock = lockNode(startNodeId);
        try {
            EdgeVector outgoingEdges = findOutgoingEdges(startNodeId);
            EdgeVector newOutgoingEdges = outgoingEdges.reindex(edge.getEdgeId().getIndex());
            setOutgoingEdges(startNodeId, newOutgoingEdges);
        } finally {
            lock.unlock();
        }
    }

    private void reindexIncoming(EdgePrimitive edge) {
        int endNodeId = edge.getEndNodeIndex();
        ReentrantLock lock = lockNode(endNodeId);
        try {
            EdgeVector incomingEdges = findIncomingEdges(endNodeId);
            EdgeVector newIncomingEdges = incomingEdges.reindex(edge.getEdgeId().getIndex());
            setIncomingEdges(endNodeId, newIncomingEdges);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public EdgeVector getOutgoingEdges(int startNodeIndex) {
        EdgeVector edges = findOutgoingEdges(startNodeIndex);
        if (edges == null) {
            edges = new EdgeVectorImpl(startNodeIndex, edgeType);
        }
        return edges;
    }

    private EdgeVector findOutgoingEdges(int startNodeIndex) {
        return edgeVectorIndex.getOutgoingEdges(startNodeIndex);
    }

    private void setOutgoingEdges(int startNodeIndex, EdgeVector outgoingEdges) {
        edgeVectorIndex.setOutgoingEdges(startNodeIndex, outgoingEdges);
    }

    @Override
    public EdgeVector getIncomingEdges(int endNodeIndex) {
        EdgeVector edges = findIncomingEdges(endNodeIndex);
        if (edges == null) {
            edges = new EdgeVectorImpl(endNodeIndex, edgeType);
            ((EdgeVectorImpl) edges).setEdgeDirection(EdgeDirection.INCOMING);
        }
        return edges;
    }

    private EdgeVector findIncomingEdges(int endNodeIndex) {
        return edgeVectorIndex.getIncomingEdges(endNodeIndex);
    }

    private void setIncomingEdges(int endNodeIndex, EdgeVector incomingEdges) {
        edgeVectorIndex.setIncomingEdges(endNodeIndex, incomingEdges);
    }

    @Override
    public EdgeType getEdgeType() {
        return edgeType;
    }

    private ReentrantLock lockNode(int nodeIndex) {
        int index = nodeIndex % locks.size();
        ReentrantLock lock = locks.get(index);
        lock.lock();
        return lock;
    }

}
