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

package org.graphit.properties.domain;

import java.util.Map;
import java.util.Set;

import org.springframework.util.Assert;

/**
 * A proxy that delegates all requests to the underlying {@link Properties}
 * instance. Can be made immutable.
 *
 * @author jon
 *
 */
public abstract class PropertiesProxy implements Properties {

    private final boolean mutable;
    private final Properties properties;

    /**
     * Creates a new instance.
     *
     * @param properties
     *            The underlying properties.
     * @param mutable
     *            If false, all attempts to modify this instance will lead to an
     *            {@link IllegalArgumentException}.
     */
    public PropertiesProxy(Properties properties, boolean mutable) {
        Assert.notNull(properties);
        this.properties = properties;
        this.mutable = mutable;
    }

    protected Properties getProperties() {
        return properties;
    }

    @Override
    public Object getProperty(String key) {
        return properties.getProperty(key);
    }

    @Override
    public void setProperty(String key, Object value) {
        Assert.isTrue(mutable, "This proxy is immutable");
        properties.setProperty(key, value);
    }

    @Override
    public Object removeProperty(String key) {
        Assert.isTrue(mutable, "This proxy is immutable");
        return properties.removeProperty(key);
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

}
