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

package org.jon.ivmark.graphit.core.properties;

import com.google.common.base.Preconditions;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A {@link Properties} implementation backed by a {@link HashMap}.
 * 
 * @author jon
 * 
 */
public class HashMapProperties implements Properties {

    private final Map<String, Object> properties;

    /**
     * Crates a new instance with a default initial capacity.
     */
    public HashMapProperties() {
        this(10);
    }

    /**
     * Crates a new instance with the provided initial capacity.
     */
    public HashMapProperties(int capacity) {
        this.properties = new HashMap<String, Object>(capacity);
    }

    /**
     * Crates a new instance with all the entries of the provided map.
     */
    public HashMapProperties(Map<String, Object> initialProperties) {
        Preconditions.checkNotNull(initialProperties);
        this.properties = new HashMap<String, Object>(initialProperties);
    }

    @Override
    public Object getProperty(String key) {
        return properties.get(key);
    }

    @Override
    public void setProperty(String key, Object value) {
        properties.put(key, value);
    }

    @Override
    public Object removeProperty(String key) {
        return properties.remove(key);
    }

    @Override
    public boolean containsProperty(String key) {
        return properties.containsKey(key);
    }

    @Override
    public Set<String> getPropertyKeys() {
        return properties.keySet();
    }

    @Override
    public Map<String, Object> asPropertyMap() {
        return new HashMap<String, Object>(properties);
    }

    @Override
    public String toString() {
        return "MapProperties [properties=" + properties + "]";
    }

    @Override
    public int size() {
        return properties.size();
    }

    @Override
    public boolean isEmpty() {
        return properties.isEmpty();
    }

}
