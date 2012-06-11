package org.opengraph.graph.edge.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.mahout.math.function.IntProcedure;
import org.apache.mahout.math.list.IntArrayList;
import org.opengraph.graph.edge.domain.EdgeId;
import org.opengraph.graph.edge.domain.EdgePrimitive;
import org.opengraph.graph.edge.domain.EdgeVector;
import org.opengraph.graph.edge.domain.EdgeVectorImpl;
import org.opengraph.graph.edge.schema.EdgeType;
import org.opengraph.graph.edge.util.EdgeIndexComparator;
import org.opengraph.graph.repository.AbstractGraphRepository;
import org.opengraph.graph.traversal.EdgeDirection;

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
public abstract class AbstractTypedEdgePrimitivesRepository extends AbstractGraphRepository
    implements TypedEdgePrimitivesRepository {

    protected final AtomicInteger maxId;
    protected final IntArrayList removedEdges;
    private final EdgeVectorRepository edgeVectorIndex;
    private final EdgeType edgeType;

    private final List<ReentrantLock> locks;

    protected AbstractTypedEdgePrimitivesRepository(EdgeType edgeType) {
        this.edgeType = edgeType;
        this.removedEdges = new IntArrayList();
        this.maxId = new AtomicInteger(-1);
        int nofLocks = Runtime.getRuntime().availableProcessors() * 2;
        List<ReentrantLock> lockList = new ArrayList<ReentrantLock>(nofLocks);
        for (int i = 0; i < nofLocks; i++) {
            lockList.add(new ReentrantLock());
        }
        this.locks = Collections.unmodifiableList(lockList);
        this.edgeVectorIndex = new ShardedEdgeVectorRepository(nofLocks);
    }

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
        int startNodeId = edge.getStartNodeId();
        ReentrantLock lock = lockNode(startNodeId);
        try {
            EdgeVector outgoingEdges = findOutgoingEdges(startNodeId);
            if (outgoingEdges == null) {
                EdgeVectorImpl edgeVector = new EdgeVectorImpl(startNodeId, edgeType);
                edgeVector.setEdgeDirection(EdgeDirection.OUTGOING);
                edgeVector.setEdgeComparator(getEdgeComparator());
                outgoingEdges = edgeVector;
            }
            EdgeVector outCopy = outgoingEdges.copy();
            int edgeId = edge.getId();
            outCopy.add(edgeId);
            setOutgoingEdges(startNodeId, outCopy);
        } finally {
            lock.unlock();
        }
    }

    private void addIncomingEdge(EdgePrimitive edge) {
        int endNodeId = edge.getEndNodeId();
        ReentrantLock lock = lockNode(endNodeId);
        try {

            EdgeVector incomingEdges = findIncomingEdges(endNodeId);
            if (incomingEdges == null) {
                EdgeVectorImpl edgeVector = new EdgeVectorImpl(endNodeId, edgeType);
                edgeVector.setEdgeDirection(EdgeDirection.INCOMING);
                edgeVector.setEdgeComparator(getEdgeComparator());
                incomingEdges = edgeVector;
            }
            EdgeVector inCopy = incomingEdges.copy();
            int edgeId = edge.getId();
            inCopy.add(edgeId);
            setIncomingEdges(endNodeId, inCopy);
        } finally {
            lock.unlock();
        }
    }

    protected void addEdge(EdgePrimitive edge) {
        // Add the undirected edge as an outgoing edge from both the start
        // and end node
        addOutgoingEdge(edge);
        addIncomingEdge(edge);
    }

    private void removeOutgoingEdge(EdgePrimitive edge) {
        int startNodeId = edge.getStartNodeId();
        ReentrantLock lock = lockNode(startNodeId);
        try {
            EdgeVector outgoingEdges = findOutgoingEdges(startNodeId);
            if (outgoingEdges != null) {
                EdgeVector outCopy = outgoingEdges.copy();
                int edgeId = edge.getId();
                outCopy.remove(edgeId);
                setOutgoingEdges(startNodeId, outCopy);
            }
        } finally {
            lock.unlock();
        }
    }

    private void removeIncomingEdge(EdgePrimitive edge) {
        int endNodeId = edge.getEndNodeId();
        ReentrantLock lock = lockNode(endNodeId);
        try {
            EdgeVector incomingEdges = findIncomingEdges(endNodeId);
            if (incomingEdges != null) {
                EdgeVector inCopy = incomingEdges.copy();
                int edgeId = edge.getId();
                inCopy.remove(edgeId);
                setIncomingEdges(endNodeId, inCopy);
            }
        } finally {
            lock.unlock();
        }
    }

    protected void removeEdge(EdgePrimitive edge) {
        int edgeId = edge.getId();
        removeOutgoingEdge(edge);
        removeIncomingEdge(edge);
        synchronized (removedEdges) {
            removedEdges.add(edgeId);
        }
    }

    protected void reindex(EdgePrimitive edge) {
        reindexOutgoing(edge);
        reindexIncoming(edge);
    }

    private void reindexOutgoing(EdgePrimitive edge) {
        int startNodeId = edge.getStartNodeId();
        ReentrantLock lock = lockNode(startNodeId);
        try {
            EdgeVector outgoingEdges = findOutgoingEdges(startNodeId);
            if (outgoingEdges != null) {
                EdgeVector outCopy = outgoingEdges.copy();
                outCopy.reindex(edge.getEdgeId().getIndex());
                setOutgoingEdges(startNodeId, outCopy);
            }
        } finally {
            lock.unlock();
        }
    }

    private void reindexIncoming(EdgePrimitive edge) {
        int endNodeId = edge.getEndNodeId();
        ReentrantLock lock = lockNode(endNodeId);
        try {
            EdgeVector incomingEdges = findIncomingEdges(endNodeId);
            if (incomingEdges != null) {
                EdgeVector inCopy = incomingEdges.copy();
                inCopy.reindex(edge.getEdgeId().getIndex());
                setIncomingEdges(endNodeId, inCopy);
            }
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

    protected EdgeVector removeOutgoingEdges(int startNodeIndex) {
        EdgeVector edges = edgeVectorIndex.removeOutgoingEdges(startNodeIndex);
        if (edges == null) {
            edges = new EdgeVectorImpl(startNodeIndex, edgeType);
        }
        edges.forEachEdgeId(new IntProcedure() {

            @Override
            public boolean apply(int edgeIndex) {
                removeEdge(new EdgeId(getEdgeType(), edgeIndex));
                return true;
            }
        });
        return edges;
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

    protected EdgeVector removeIncomingEdges(int endNodeIndex) {
        EdgeVector edges = edgeVectorIndex.removeIncomingEdges(endNodeIndex);
        if (edges == null) {
            edges = new EdgeVectorImpl(endNodeIndex, edgeType);
            ((EdgeVectorImpl) edges).setEdgeDirection(EdgeDirection.INCOMING);
        }
        edges.forEachEdgeId(new IntProcedure() {

            @Override
            public boolean apply(int edgeIndex) {
                removeEdge(new EdgeId(getEdgeType(), edgeIndex));
                return true;
            }
        });
        return edges;
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

    protected abstract EdgeIndexComparator getEdgeComparator();
}
