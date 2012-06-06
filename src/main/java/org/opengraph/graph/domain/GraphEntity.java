package org.opengraph.graph.domain;

import org.opengraph.properties.domain.Properties;

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
