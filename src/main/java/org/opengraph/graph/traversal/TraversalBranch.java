package org.opengraph.graph.traversal;

import java.util.LinkedList;

import org.opengraph.graph.edge.domain.Edge;

/**
 * A traversed graph branch.
 * 
 * @author jon
 * 
 */
public interface TraversalBranch {

    int getDepth();

    double getTotalWeight();

    Edge getCurrentEdge();

    TraversalBranch append(Edge edge);

    LinkedList<Edge> asList();

}