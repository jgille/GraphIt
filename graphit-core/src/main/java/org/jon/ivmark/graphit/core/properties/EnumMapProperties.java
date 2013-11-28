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

import java.util.*;

/**
 * A {@link Properties} implementation backed by a {@link EnumMap}.
 *
 * @author jon
 *
 */
public class EnumMapProperties<E extends Enum<E>> implements Properties {

    private final Class<E> enumClass;
    private final EnumMap<E, Object> properties;

    /**
     * Crates a new instance with.
     */
    public EnumMapProperties(Class<E> enumClass) {
        this.enumClass = enumClass;
        this.properties = new EnumMap<E, Object>(enumClass);
    }

    private E getKey(String keyName) {
        return Enum.valueOf(enumClass, keyName);
    }

    @Override
    public Object getProperty(String keyName) {
        return getProperty(getKey(keyName));
    }

    /**
     * Gets a property.
     */
    public Object getProperty(E key) {
        return properties.get(key);
    }

    @Override
    public void setProperty(String keyName, Object value) {
        setProperty(getKey(keyName), value);
    }

    /**
     * Sets a property.
     */
    public void setProperty(E key, Object value) {
        properties.put(key, value);
    }

    @Override
    public Object removeProperty(String keyName) {
        return removeProperty(getKey(keyName));
    }

    /**
     * Removes a property.
     */
    public Object removeProperty(E key) {
        return properties.remove(key);
    }

    @Override
    public boolean containsProperty(String keyName) {
        return containsProperty(getKey(keyName));
    }

    /**
     * Returns true if this instance contains the given key.
     */
    public boolean containsProperty(E key) {
        return properties.containsKey(key);
    }

    @Override
    public Set<String> getPropertyKeys() {
        Set<String> res = new HashSet<String>();
        for (Enum<E> key : properties.keySet()) {
            res.add(key.name());
        }
        return res;
    }

    @Override
    public Map<String, Object> asPropertyMap() {
        Map<String, Object> res = new HashMap<String, Object>();
        for (Map.Entry<E, Object> e : properties.entrySet()) {
            Object value = e.getValue();
            E key = e.getKey();
            res.put(key.name(), value);
        }
        return res;
    }

    @Override
    public String toString() {
        return "EnumMapProperties [enumClass=" + enumClass + ", properties=" + properties + "]";
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
