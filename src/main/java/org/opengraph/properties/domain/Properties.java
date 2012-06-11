package org.opengraph.properties.domain;

import java.util.Map;
import java.util.Set;

/**
 * A set of key/value properties.
 *
 * @author jon
 *
 */
public interface Properties {

    /**
     * Gets a property for a key.
     */
    Object getProperty(String key);

    /**
     * Sets a property for a key.
     */
    void setProperty(String key, Object value);

    /**
     * Removes a property for a key.
     */
    Object removeProperty(String key);

    /**
     * Checks the existance of a key.
     */
    boolean containsProperty(String key);

    /**
     * Gets all keys for this container.
     */
    Set<String> getPropertyKeys();

    /**
     * Gets properties as a map.
     */
    Map<String, Object> asPropertyMap();

}
