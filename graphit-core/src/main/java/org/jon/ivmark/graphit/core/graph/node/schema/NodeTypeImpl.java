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

package org.jon.ivmark.graphit.core.graph.node.schema;

import org.jon.ivmark.graphit.core.schema.AbstractGraphType;
import org.jon.ivmark.graphit.core.properties.domain.HashMapPropertiesFactory;
import org.jon.ivmark.graphit.core.properties.domain.PropertiesFactory;

/**
 * Standard {@link NodeType} implementation.
 *
 * @author jon
 *
 */
public class NodeTypeImpl extends AbstractGraphType implements NodeType {

    private final PropertiesFactory propertiesFactory;

    /**
     * Creates a new instance using a {@link HashMapPropertiesFactory}.
     */
    public NodeTypeImpl(String name) {
        this(name, new HashMapPropertiesFactory());
    }

    /**
     * Creates a new instance.
     */
    public NodeTypeImpl(String name, PropertiesFactory propertiesFactory) {
        super(name);
        this.propertiesFactory = propertiesFactory;
    }

    @Override
    public String toString() {
        return "NodeTypeImpl [name()=" + name() + "]";
    }

    @Override
    public PropertiesFactory getPropertiesFactory() {
        return propertiesFactory;
    }

}
