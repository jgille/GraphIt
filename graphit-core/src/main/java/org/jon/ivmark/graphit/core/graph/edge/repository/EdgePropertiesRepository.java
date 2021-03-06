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

package org.jon.ivmark.graphit.core.graph.edge.repository;

import org.jon.ivmark.graphit.core.graph.edge.EdgeId;
import org.jon.ivmark.graphit.core.properties.Properties;
import org.jon.ivmark.graphit.core.properties.repository.ConcurrentHashMapPropertiesRepository;

/**
 * A repository storing edge properties in a {@link java.util.concurrent.ConcurrentHashMap}.
 *
 * @author jon
 * 
 */
public class EdgePropertiesRepository extends ConcurrentHashMapPropertiesRepository<EdgeId> {

    /**
     * Creates a new instance.
     */
    public EdgePropertiesRepository(int initalCapacity) {
        super(initalCapacity);
    }

    @Override
    protected Properties createEmptyProperties(EdgeId id) {
        return id.getEdgeType().getPropertiesFactory().createEmptyProperties();
    }

}
