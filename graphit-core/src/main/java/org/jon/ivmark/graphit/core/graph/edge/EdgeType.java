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

package org.jon.ivmark.graphit.core.graph.edge;

import org.jon.ivmark.graphit.core.graph.entity.GraphEntityType;
import org.jon.ivmark.graphit.core.properties.DynamicEnumerableElement;
import org.jon.ivmark.graphit.core.properties.HashMapPropertiesFactory;
import org.jon.ivmark.graphit.core.properties.PropertiesFactory;

/**
 * An edge type. Defaults to being unweighted and unsorted.
 *
 * @author jon
 *
 */
public class EdgeType extends GraphEntityType implements DynamicEnumerableElement {

    private final EdgeSortOrder sortOrder;
    private final PropertiesFactory propertiesFactory;

    /**
     * Creates an unsorted edge type using a {@link HashMapPropertiesFactory}.
     */
    public EdgeType(String name) {
        this(name, EdgeSortOrder.UNDEFINED);
    }

    /**
     * Creates an edge type using a {@link HashMapPropertiesFactory}.
     */
    public EdgeType(String name, EdgeSortOrder sortOrder) {
        this(name, sortOrder, new HashMapPropertiesFactory());
    }

    /**
     * Creates an edge type.
     */
    public EdgeType(String name, EdgeSortOrder sortOrder, PropertiesFactory propertiesFactory) {
        super(name);
        this.sortOrder = sortOrder;
        this.propertiesFactory = propertiesFactory;
    }

    public EdgeSortOrder getSortOrder() {
        return sortOrder;
    }

    @Override
    public String toString() {
        return "EdgeType [sortOrder=" + sortOrder + ", name()=" + name() + "]";
    }

    public PropertiesFactory getPropertiesFactory() {
        return propertiesFactory;
    }

}
