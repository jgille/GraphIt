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

package org.graphit.properties.repository;


import org.graphit.graph.node.domain.NodeId;
import org.graphit.properties.domain.Properties;

/**
 * A repository storing edge properties in a {@link ConcurrentHashMap}.
 *
 * @author jon
 *
 */
public class NodePropertiesRepository extends ConcurrentHashMapPropertiesRepository<NodeId> {

    /**
     * Creates a new instance.
     */
    public NodePropertiesRepository(int initalCapacity) {
        super(initalCapacity);
    }

    @Override
    protected Properties createEmptyProperties(NodeId id) {
        return id.getNodeType().getPropertiesFactory().createEmptyProperties();
    }

}
