package org.opengraph.graph.traversal;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.mahout.math.list.FloatArrayList;
import org.apache.mahout.math.list.LongArrayList;
import org.opengraph.graph.edge.domain.Edge;
import org.opengraph.graph.edge.domain.EdgeId;

public class PrimitivesTraversalBranch implements TraversalBranch {

    private final List<EdgeId> edgeIds;
    private final LongArrayList startAndEndNodes;
    private final FloatArrayList weights;

    private PrimitivesTraversalBranch(Edge initialEdge) {
        int capacity = 10;
        this.edgeIds = new ArrayList<EdgeId>(capacity);
        this.startAndEndNodes = new LongArrayList(capacity);
        this.weights = new FloatArrayList(capacity);
    }

    @Override
    public int getDepth() {
        return edgeIds.size();
    }

    @Override
    public double getTotalWeight() {
        float sum = 0f;
        for (float weight : weights.elements()) {
            sum += weight;
        }
        return sum;
    }

    @Override
    public Edge getCurrentEdge() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TraversalBranch append(Edge edge) {
        edgeIds.add(edge.getEdgeId());
        // start node in first 4 bytes, end node in last 4 bytes
        long startAndEndNode =
            (edge.getStartNode().getIndex() << 32) | edge.getEndNode().getIndex();
        startAndEndNodes.add(startAndEndNode);
        weights.add(edge.getWeight());
        return this;
    }

    @Override
    public LinkedList<Edge> asList() {
        // TODO Auto-generated method stub
        return null;
    }

}
