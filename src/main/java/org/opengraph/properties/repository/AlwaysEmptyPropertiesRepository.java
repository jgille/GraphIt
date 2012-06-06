package org.opengraph.properties.repository;

import org.opengraph.properties.domain.MapProperties;
import org.opengraph.properties.domain.Properties;

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
