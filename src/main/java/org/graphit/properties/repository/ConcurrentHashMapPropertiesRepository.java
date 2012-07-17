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

import org.graphit.properties.domain.HashMapPropertiesFactory;
import org.graphit.properties.domain.Properties;
import org.graphit.properties.domain.PropertiesFactory;

/**
 * A {@link PropertiesRepository} backed by a {@link ConcurrentHashMap}.
 * 
 * @author jon
 * 
 */
public class ConcurrentHashMapPropertiesRepository<T>
    implements PropertiesRepository<T> {

    private static final float LOAD_FACTOR = 0.75f;

    private final ConcurrentHashMap<T, Properties> repo;
    private PropertiesFactory propertiesFactory;

    /**
     * Creates an empty repo with the specified initial capacity.
     * 
     * @param initalCapacity
     */
    public ConcurrentHashMapPropertiesRepository(int initalCapacity) {
        this.repo =
            new ConcurrentHashMap<T, Properties>(initalCapacity, LOAD_FACTOR, Runtime.getRuntime()
                .availableProcessors() * 4);
        this.propertiesFactory = new HashMapPropertiesFactory();
    }

    /**
     * Gets the factory used to create new {@link Properties} instances.
     */
    PropertiesFactory getPropertiesFactory() {
        return propertiesFactory;
    }

    /**
     * Sets a custom factory used to create new {@link Properties} instances.
     */
    public void setPropertiesFactory(PropertiesFactory propertiesFactory) {
        this.propertiesFactory = propertiesFactory;
    }

    @Override
    public Properties getProperties(T id) {
        Properties properties = repo.get(id);
        if (properties != null) {
            return properties;
        }
        return propertiesFactory.createEmptyProperties();
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
        return propertiesFactory.createEmptyProperties();
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

    private synchronized Properties getOrAddProperties(T id) {
        Properties properties;
        if (repo.containsKey(id)) {
            properties = repo.get(id);
        } else {
            properties = propertiesFactory.createEmptyProperties();
            repo.put(id, properties);
        }
        return properties;
    }
}
