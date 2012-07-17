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

/**
 * A set of key/value properties.
 * 
 * @author jon
 * 
 */
public interface Properties {

    /**
     * Gets a property for a key.
     */
    Object getProperty(String key);

    /**
     * Sets a property for a key.
     */
    void setProperty(String key, Object value);

    /**
     * Removes a property for a key.
     */
    Object removeProperty(String key);

    /**
     * Checks the existance of a key.
     */
    boolean containsProperty(String key);

    /**
     * Gets all keys for this container.
     */
    Set<String> getPropertyKeys();

    /**
     * Gets the number of entries in this instance.
     */
    int size();

    /**
     * Returns if this instance has no entries.
     */
    boolean isEmpty();

    /**
     * Gets properties as a map.
     */
    Map<String, Object> asPropertyMap();

}
