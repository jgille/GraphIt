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

package org.jon.ivmark.graphit.core.graph.entity;

import org.jon.ivmark.graphit.core.properties.Properties;

/**
 * An entity in a graph, e.g. a node or an edge.
 * 
 * @author jon
 * 
 * @param <T>
 *            The generic type of the entity type.
 */
public interface GraphEntity<T> extends Properties {

    /**
     * Gets the type of this entity.
     */
    T getType();

    /**
     * Gets the internally used index of this entity.
     */
    int getIndex();

}
