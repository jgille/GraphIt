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

package org.jon.ivmark.graphit.core.graph.node;

import org.jon.ivmark.graphit.core.graph.entity.GraphEntityType;
import org.jon.ivmark.graphit.core.properties.HashMapPropertiesFactory;
import org.jon.ivmark.graphit.core.properties.PropertiesFactory;

/**
 * Describes the type of a node in a graph.
 *
 * @author jon
 *
 */
public class NodeType extends GraphEntityType {

    private final PropertiesFactory propertiesFactory;

    /**
     * Creates a new instance using a {@link HashMapPropertiesFactory}.
     */
    public NodeType(String name) {
        this(name, new HashMapPropertiesFactory());
    }

    /**
     * Creates a new instance.
     */
    public NodeType(String name, PropertiesFactory propertiesFactory) {
        super(name);
        this.propertiesFactory = propertiesFactory;
    }

    @Override
    public String toString() {
        return "NodeType [name()=" + name() + "]";
    }

    /**
     * Returns a factory used to create new {@link org.jon.ivmark.graphit.core.properties.Properties} instances for
     * nodes.
     */
    public PropertiesFactory getPropertiesFactory() {
        return propertiesFactory;
    }

}
