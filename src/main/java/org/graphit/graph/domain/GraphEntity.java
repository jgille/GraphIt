package org.graphit.graph.domain;

import org.graphit.properties.domain.Properties;

/**
 * An entity in a graph, e.g. a node or an edge.
 *
 * @author jon
 *
 * @param <T>
 *            The generic type of the entity type.
 */
public interface GraphEntity<T> extends Properties {

    /**
     * Gets the type of this entity.
     */
    T getType();

    /**
     * Gets the internally used index of this entity.
     */
    int getIndex();

}
