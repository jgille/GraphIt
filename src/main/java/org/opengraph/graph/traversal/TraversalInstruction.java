package org.opengraph.graph.traversal;

import org.springframework.util.Assert;

/**
 * Instructions for a traverser on how, if at all, to continue traversing from a
 * node.
 *
 * @author jon
 *
 */
public class TraversalInstruction {

    private final boolean shouldContinueTraversal;
    private final TraversalEdgeDescriptor nextEdgeDescription;

    TraversalInstruction(boolean shouldContinueTraversal, TraversalEdgeDescriptor nextEdgeDescription) {
        this.shouldContinueTraversal = shouldContinueTraversal;
        if (shouldContinueTraversal) {
            Assert.notNull(nextEdgeDescription,
                           "En edge description for continued traversal must be provided.");
        }
        this.nextEdgeDescription = nextEdgeDescription;
    }

    /**
     * Returns true if the traverser should continue traversing from the node.
     */
    public boolean getShouldContinueTraversal() {
        return shouldContinueTraversal;
    }

    /**
     * Gets a description of the edges to traverse from the node
     */
    public TraversalEdgeDescriptor getNextEdgeDescription() {
        return nextEdgeDescription;
    }
}
