package org.opengraph.graph.traversal;

/**
 * Instructs a traverser how, if at all, to continue traversing from the end
 * node of a {@link SimpleTraversalBranch}.
 *
 * @author jon
 *
 */
public interface TraversalInstructor {

    /**
     * Gets an instruction for how, if at all, to continue traversal from the
     * end node of the {@link SimpleTraversalBranch}.
     */
    TraversalInstruction getFurtherTraversalInstruction(TraversalBranch branch);
}
