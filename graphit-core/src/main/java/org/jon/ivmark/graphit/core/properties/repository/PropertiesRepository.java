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

package org.jon.ivmark.graphit.core.properties.repository;

import org.jon.ivmark.graphit.core.properties.domain.Properties;

/**
 * A repository for properties, e.g. node or edge properties.
 * 
 * @author jon
 * 
 * @param <T>
 *            The generic type of the id of each entry in this repo.
 */
public interface PropertiesRepository<T> {

    /**
     * Gets all properties for an entry.
     */
    Properties getProperties(T id);

    /**
     * Saves properties for an entry, overwriting any previously store value.
     */
    void saveProperties(T id, Properties properties);

    /**
     * Removes all properties for an entry.
     */
    Properties removeProperties(T id);

    /**
     * Sets an individual property for an entry, leaving other properties
     * untouched.
     */
    void setProperty(T id, String key, Object value);

    /**
     * Removes an individual property for an entry, leaving other properties
     * untouched.
     */
    void removeProperty(T id, String key);

}
