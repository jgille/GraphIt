package org.graphit.properties.repository;

import org.graphit.properties.domain.MapProperties;
import org.graphit.properties.domain.Properties;

/**
 * A {@link PropertiesRepository} that will always return empty
 * {@link Properties} instances.
 *
 * @author jon
 *
 * @param <T>
 *            The generic type of the id of each entry in this repo.
 */
public class AlwaysEmptyPropertiesRepository<T> implements PropertiesRepository<T> {

    @Override
    public Properties getProperties(T id) {
        return new MapProperties();
    }

    @Override
    public void saveProperties(T id, Properties properties) {
    }

    @Override
    public Properties removeProperties(T id) {
        return new MapProperties();
    }

    @Override
    public void setProperty(T id, String key, Object value) {
    }

    @Override
    public void removeProperty(T id, String key) {
    }

}
