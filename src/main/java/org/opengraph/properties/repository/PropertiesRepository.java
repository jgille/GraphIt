package org.opengraph.properties.repository;

import org.opengraph.properties.domain.Properties;

/**
 * A repository for properties, e.g. node or edge properties.
 *
 * @author jon
 *
 * @param <T>
 *            The generic type of the id of each entry in this repo.
 */
public interface PropertiesRepository<T> {

    /**
     * Gets all properties for an entry.
     */
    Properties getProperties(T id);

    /**
     * Saves properties for an entry, overwriting any previously store value.
     */
    void saveProperties(T id, Properties properties);

    /**
     * Removes all properties for an entry.
     */
    Properties removeProperties(T id);

    /**
     * Sets an individual property for an entry, leaving other properties
     * untouched.
     */
    void setProperty(T id, String key, Object value);

    /**
     * Removes an individual property for an entry, leaving other properties
     * untouched.
     */
    void removeProperty(T id, String key);

}
