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

package org.jon.ivmark.graphit.recommendation;

import org.jon.ivmark.graphit.core.properties.Properties;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ImmutableItemProperties implements Properties {

    private final Map<String, Object> properties;

    public ImmutableItemProperties(Item item) {
        this.properties = item.getProperties() == null ? new HashMap<String, Object>() : item.getProperties();
    }

    @Override
    public Object getProperty(String key) {
        return properties.get(key);
    }

    @Override
    public void setProperty(String key, Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object removeProperty(String key) {
        throw new UnsupportedOperationException();
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
    public int size() {
        return properties.size();
    }

    @Override
    public boolean isEmpty() {
        return properties.isEmpty();
    }

    @Override
    public Map<String, Object> asPropertyMap() {
        return Collections.unmodifiableMap(properties);
    }
}
