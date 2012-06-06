package org.opengraph.common.predicates;

public interface Predicate<T> {

    boolean accepts(T value);
}
