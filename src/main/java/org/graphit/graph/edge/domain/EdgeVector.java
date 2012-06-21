package org.graphit.graph.edge.domain;

import java.util.List;

import org.apache.mahout.math.function.IntProcedure;
import org.graphit.graph.edge.schema.EdgeType;
import org.graphit.graph.traversal.EdgeDirection;
import org.springframework.core.convert.converter.Converter;

/**
 * A vector of edges originating from a node.
 *
 * @author jon
 */
public interface EdgeVector {

    /**
     * Gets the unique id of the root node for the edges in this vector.
     */
    int getRootNode();

    /**
     * Gets the edge type for the edges in this vector.
     */
    EdgeType getEdgeType();

    /**
     * Gets the direction of the edges (incoming/outgoing).
     */
    EdgeDirection getEdgeDirection();

    /**
     * Returns a deep copy of this edge vector.
     */
    EdgeVector copy();

    /**
     * Adds an edge to this vector.
     *
     * @param edgeId
     *            The unique id for the new edge.
     */
    void add(int edgeId);

    /**
     * Removes an edge.
     *
     * @param edgeId
     *            The unique id for the edge that should be removed.
     */
    void remove(int edgeId);

    /**
     * Reindex an edge in this vector.
     *
     * @param edgeId
     *            The unique id for the edge that should be removed.
     */
    void reindex(int edgeId);

    /**
     * Applies the procedure for each edge id in this vector. Stops once a
     * procedure call returns false.
     *
     * @param procedure
     *            The procedure to apply for each edge id.
     */
    void forEachEdgeId(IntProcedure procedure);

    /**
     * Gets all edges as a list.
     */
    List<Integer> asList();

    /**
     * Gets the number of edges.
     */
    int size();

    /**
     * Returns true if this vector contains no edges, false otherwise.
     */
    boolean isEmpty();

    /**
     * Returns an iterable of this vector, converting each element with the
     * provided {@link Converter}.
     */
    <T> Iterable<T> iterable(Converter<EdgeId, T> converter);

}
