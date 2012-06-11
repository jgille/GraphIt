package org.opengraph.properties.repository;

import java.util.Map;
import java.util.Set;

import org.opengraph.properties.domain.MapProperties;
import org.opengraph.properties.domain.Properties;

/**
 * A {@link Properties} implementation that will notify a
 * {@link PropertiesRepository} of all changes.
 *
 * @author jon
 *
 * @param <T>
 *            The generic type of the id of this instance.
 */
public class WriteThroughProperties<T> implements Properties {

    private final T id;
    private final Properties properties;
    private final PropertiesRepository<T> repo;

    public WriteThroughProperties(T id, Properties properties, PropertiesRepository<T> repo) {
        this.id = id;
        this.properties = properties == null ? new MapProperties() : properties;
        this.repo = repo;
    }

    @Override
    public Object getProperty(String key) {
        return properties.getProperty(key);
    }

    @Override
    public void setProperty(String key, Object value) {
        properties.setProperty(key, value);
        repo.setProperty(id, key, value);
    }

    @Override
    public Object removeProperty(String key) {
        Object property = properties.removeProperty(key);
        repo.removeProperty(id, key);
        return property;
    }

    @Override
    public boolean containsProperty(String key) {
        return properties.containsProperty(key);
    }

    @Override
    public Set<String> getPropertyKeys() {
        return properties.getPropertyKeys();
    }

    @Override
    public Map<String, Object> asPropertyMap() {
        return properties.asPropertyMap();
    }

    @Override
    public String toString() {
        return "WriteThroughProperties [id=" + id + ", properties=" + properties + "]";
    }

}
