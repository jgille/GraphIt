package org.opengraph.graph.traversal;

import java.util.LinkedList;

import org.opengraph.graph.edge.domain.Edge;

/**
 * A traversed graph branch.
 *
 * @author jon
 *
 */
public class SimpleTraversalBranch implements TraversalBranch {

    private final int depth;
    private final double totalWeight;
    private SimpleTraversalBranch parent;
    private final Edge currentEdge;

    public static TraversalBranch start(Edge edge) {
        return new SimpleTraversalBranch(edge);
    }

    private SimpleTraversalBranch(Edge edge) {
        this.currentEdge = edge;
        this.totalWeight = edge.getWeight();
        this.depth = 1;
    }

    private SimpleTraversalBranch(SimpleTraversalBranch parent, Edge currentEdge) {
        this.currentEdge = currentEdge;
        this.parent = parent;
        this.depth = parent.getDepth() + 1;
        this.totalWeight = parent.getTotalWeight() + currentEdge.getWeight();
    }

    @Override
    public int getDepth() {
        return depth;
    }

    @Override
    public double getTotalWeight() {
        return totalWeight;
    }

    public SimpleTraversalBranch getParent() {
        return parent;
    }

    @Override
    public Edge getCurrentEdge() {
        return currentEdge;
    }

    @Override
    public TraversalBranch append(Edge edge) {
        return new SimpleTraversalBranch(this, edge);
    }

    public boolean isRoot() {
        return parent == null;
    }

    @Override
    public LinkedList<Edge> asList() {
        LinkedList<Edge> path = new LinkedList<Edge>();
        path.addFirst(currentEdge);
        SimpleTraversalBranch branch = parent;
        while (branch != null) {
            path.add(branch.getCurrentEdge());
            branch = branch.getParent();
        }
        return path;
    }

    @Override
    public String toString() {
        return "TraversalBranch [depth=" + depth + ", totalWeight=" + totalWeight
            + ", currentEdge=" + currentEdge + "]";
    }

}
