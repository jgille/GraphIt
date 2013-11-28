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

import java.util.concurrent.ConcurrentHashMap;

import org.graphit.properties.domain.Properties;

/**
 * A {@link PropertiesRepository} backed by a {@link ConcurrentHashMap}.
 *
 * @author jon
 *
 */
public abstract class ConcurrentHashMapPropertiesRepository<T>
    implements PropertiesRepository<T> {

    private static final float LOAD_FACTOR = 0.75f;

    private final ConcurrentHashMap<T, Properties> repo;

    /**
     * Creates an empty repo with the specified initial capacity.
     */
    public ConcurrentHashMapPropertiesRepository(int initalCapacity) {
        this.repo =
            new ConcurrentHashMap<T, Properties>(initalCapacity, LOAD_FACTOR);
    }

    /**
     * Createa a new empty {@link Properties} instances.
     */
    protected abstract Properties createEmptyProperties(T id);

    @Override
    public Properties getProperties(T id) {
        Properties properties = repo.get(id);
        if (properties != null) {
            return properties;
        }
        return createEmptyProperties(id);
    }

    @Override
    public void saveProperties(T id, Properties properties) {
        repo.put(id, properties);
    }

    @Override
    public Properties removeProperties(T id) {
        Properties properties = repo.remove(id);
        if (properties != null) {
            return properties;
        }
        return createEmptyProperties(id);
    }

    @Override
    public void setProperty(T id, String key, Object value) {
        Properties properties = getOrAddProperties(id);
        properties.setProperty(key, value);
    }

    @Override
    public void removeProperty(T id, String key) {
        Properties properties = getOrAddProperties(id);
        properties.removeProperty(key);
    }

    private Properties getOrAddProperties(T id) {
        Properties properties = repo.get(id);
        if (properties != null) {
            return properties;
        }
        properties = createEmptyProperties(id);
        repo.put(id, properties);
        return properties;
    }
}
