/*
 * Copyright 2012 Jon Ivmark
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.graphit.properties.repository;

import java.util.Map;
import java.util.Set;

import org.graphit.properties.domain.HashMapProperties;
import org.graphit.properties.domain.Properties;

/**
 * A {@link Properties} implementation that will propagate all changes to a
 * {@link PropertiesRepository}.
 *
 * @author jon
 *
 * @param <T>
 *            The generic type of the id of this instance.
 */
public class WriteThroughProperties<T> implements Properties {

    private final T id;
    private Properties properties;
    private final PropertiesRepository<T> repo;

    /**
     * Creates a new lazy instance, i.e. it will load properties from the repo
     * on first request.
     *
     * @param id
     *            The id of this instance.
     * @param repo
     *            A repo to propagate writes to.
     */
    public WriteThroughProperties(T id, PropertiesRepository<T> repo) {
        this.id = id;
        this.repo = repo;
    }

    /**
     * Creates a new instance.
     *
     * @param id
     *            The id of this instance.
     * @param properties
     *            The initial properties.
     * @param repo
     *            A repo to propagate writes to.
     */
    public WriteThroughProperties(T id, Properties properties, PropertiesRepository<T> repo) {
        this.id = id;
        this.properties = properties == null ? new HashMapProperties() : properties;
        this.repo = repo;
    }

    @Override
    public Object getProperty(String key) {
        return loadOrGetProperties().getProperty(key);
    }

    @Override
    public void setProperty(String key, Object value) {
        loadOrGetProperties().setProperty(key, value);
        repo.setProperty(id, key, value);
    }

    @Override
    public Object removeProperty(String key) {
        Object property = loadOrGetProperties().removeProperty(key);
        repo.removeProperty(id, key);
        return property;
    }

    @Override
    public boolean containsProperty(String key) {
        return loadOrGetProperties().containsProperty(key);
    }

    @Override
    public Set<String> getPropertyKeys() {
        return loadOrGetProperties().getPropertyKeys();
    }

    @Override
    public Map<String, Object> asPropertyMap() {
        return loadOrGetProperties().asPropertyMap();
    }

    @Override
    public String toString() {
        return "WriteThroughProperties [id=" + id + ", properties=" + properties + "]";
    }

    private Properties loadOrGetProperties() {
        if (properties != null) {
            return properties;
        }
        properties = repo.getProperties(id);
        if (properties == null) {
            properties = new HashMapProperties();
        }
        return properties;
    }

    @Override
    public int size() {
        return loadOrGetProperties().size();
    }

    @Override
    public boolean isEmpty() {
        return loadOrGetProperties().isEmpty();
    }
}
