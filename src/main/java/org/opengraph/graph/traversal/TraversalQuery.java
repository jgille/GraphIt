package org.opengraph.graph.traversal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 *
 * A query defining rules for a graph traversal.
 *
 * @author jon
 *
 */
public class TraversalQuery {

    private TraversalOrder traversalOrder = TraversalOrder.BREADTH_FIRST;
    private int maxTraversalDepth = 1;
    private Collection<String> startNodeIds;
    private TraversalInstructor instructor;

    /**
     * Gets the traversal order.
     */
    public TraversalOrder getTraversalOrder() {
        return traversalOrder;
    }

    /**
     * Sets the traversal order.
     */
    public TraversalQuery setTraversalOrder(TraversalOrder traversalOrder) {
        this.traversalOrder = traversalOrder;
        return this;
    }

    /**
     * Gets the maximum depth of the traversal, i.e. how many connected edges to
     * traverse from the start node(s).
     *
     */
    public int getMaxTraversalDepth() {
        return maxTraversalDepth;
    }

    /**
     * Gets the maximum depth of the traversal, i.e. how many (consecutively)
     * connected edges to traverse from the start node(s).
     * 
     */
    public void setMaxTraversalDepth(int maxTraversalDepth) {
        this.maxTraversalDepth = maxTraversalDepth;
    }

    /**
     * Gets the ids of the nodes to start traversing from.
     */
    public Collection<String> getStartNodeIds() {
        return startNodeIds;
    }

    /**
     * Sets the ids of the nodes to start traversing from.
     */
    public TraversalQuery setStartNodeIds(Collection<String> startNodeIds) {
        this.startNodeIds = new ArrayList<String>(startNodeIds);
        return this;
    }

    /**
     * Convenience method delegating to {@link #setStartNodeIds(Collection)}.
     */
    public TraversalQuery setStartNodeIds(String... startNodeIds) {
        this.startNodeIds = Arrays.asList(startNodeIds);
        return this;
    }

    public TraversalInstructor getInstructor() {
        return instructor;
    }

    public TraversalQuery setInstructor(TraversalInstructor instructor) {
        this.instructor = instructor;
        return this;
    }

}
