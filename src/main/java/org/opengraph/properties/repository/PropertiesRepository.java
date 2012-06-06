package org.opengraph.properties.repository;

import org.opengraph.properties.domain.Properties;

public interface PropertiesRepository<T> {

    Properties getProperties(T id);

    void saveProperties(T id, Properties properties);

    Properties removeProperties(T id);

    void setProperty(T id, String key, Object value);

    void removeProperty(T id, String key);

}
