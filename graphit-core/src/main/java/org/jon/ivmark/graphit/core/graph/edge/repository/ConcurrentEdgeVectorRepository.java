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

import org.apache.mahout.math.map.AbstractIntObjectMap;
import org.apache.mahout.math.map.OpenIntObjectHashMap;
import org.jon.ivmark.graphit.core.graph.edge.EdgeVector;

import java.util.ArrayList;
import java.util.List;

/**
 * An {@link EdgeVectorRepository} that is split into multiple segments for better
 * concurrency.
 *
 */
public class ConcurrentEdgeVectorRepository implements EdgeVectorRepository {

    private final int nofSegments;
    private final List<AbstractIntObjectMap<EdgeVector>> outgoingSegments;
    private final List<AbstractIntObjectMap<EdgeVector>> incomingSegments;

    /**
     * Creates a new repo with the provided number of segments.
     */
    public ConcurrentEdgeVectorRepository(int concurrencyLevel) {
        this.nofSegments = concurrencyLevel;
        this.outgoingSegments = new ArrayList<AbstractIntObjectMap<EdgeVector>>(concurrencyLevel);
        this.incomingSegments = new ArrayList<AbstractIntObjectMap<EdgeVector>>(concurrencyLevel);

        for (int i = 0; i < concurrencyLevel; i++) {
            this.outgoingSegments.add(new OpenIntObjectHashMap<EdgeVector>());
            this.incomingSegments.add(new OpenIntObjectHashMap<EdgeVector>());
        }
    }

    private int getSegmentIndex(int nodeIndex) {
        return nodeIndex % nofSegments;
    }

    private AbstractIntObjectMap<EdgeVector> getOutgoingSegment(int nodeIndex) {
        int segmentIndex = getSegmentIndex(nodeIndex);
        return outgoingSegments.get(segmentIndex);
    }

    private AbstractIntObjectMap<EdgeVector> getIncomingSegment(int nodeIndex) {
        int segmentIndex = getSegmentIndex(nodeIndex);
        return incomingSegments.get(segmentIndex);
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

    @Override
    public EdgeVector getOutgoingEdges(int startNodeIndex) {
        return get(getOutgoingSegment(startNodeIndex), startNodeIndex);
    }

    @Override
    public EdgeVector getIncomingEdges(int endNodeIndex) {
        return get(getIncomingSegment(endNodeIndex), endNodeIndex);
    }

    @Override
    public void setOutgoingEdges(int endNodeIndex, EdgeVector edges) {
        int startNodeIndex = edges.getRootNode();
        put(getOutgoingSegment(startNodeIndex), startNodeIndex, edges);
    }

    @Override
    public void setIncomingEdges(int endNodeIndex, EdgeVector edges) {
        put(getIncomingSegment(endNodeIndex), endNodeIndex, edges);
    }

}
