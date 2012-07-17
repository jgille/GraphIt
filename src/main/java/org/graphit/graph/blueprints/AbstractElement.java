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

package org.graphit.graph.blueprints;

import java.util.Set;

import org.graphit.properties.domain.Properties;
import org.springframework.util.Assert;

import com.tinkerpop.blueprints.Element;

/**
 * Base implementation of a blueprints {@link Element}.
 * 
 * @author jon
 * 
 * @param <T>
 *            The generic type of the element id.
 */
public abstract class AbstractElement<T> implements Element {

    private final T id;
    private final Properties properties;

    /**
     * Constructs an element.
     * 
     * @param id
     *            The id of this element.
     * @param properties
     *            The properties of this element.
     * 
     */
    protected AbstractElement(T id, Properties properties) {
        Assert.notNull(id);
        this.id = id;
        this.properties = properties;
    }

    @Override
    public Object getProperty(String key) {
        return properties.getProperty(key);
    }

    @Override
    public Set<String> getPropertyKeys() {
        return properties.getPropertyKeys();
    }

    @Override
    public void setProperty(String key, Object value) {
        if (key.equalsIgnoreCase("id")) {
            throw new IllegalArgumentException("Reserved key: " + key);
        }
        properties.setProperty(key, value);
    }

    @Override
    public Object removeProperty(String key) {
        return properties.removeProperty(key);
    }

    @Override
    public T getId() {
        return id;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        AbstractElement<?> other = (AbstractElement<?>) obj;
        return id.equals(other.id);
    }

}
