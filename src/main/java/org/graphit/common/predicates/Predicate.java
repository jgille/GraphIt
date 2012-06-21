package org.graphit.common.predicates;

/**
 * A predicate used in decision making/filtering.
 *
 * @author jon
 *
 * @param <T>
 *            The generic type for the objects this predicate handles.
 */
public interface Predicate<T> {

    /**
     * Applies the predicate.
     */
    boolean accepts(T value);
}
