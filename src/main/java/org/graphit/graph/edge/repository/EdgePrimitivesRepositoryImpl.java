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

package org.graphit.graph.edge.repository;

import org.graphit.graph.edge.schema.EdgeType;
import org.graphit.graph.edge.schema.EdgeTypes;

/**
 *
 * An {@link EdgePrimitivesRepository} backed by
 * {@link TypedEdgePrimitivesRepositoryImpl} instances.
 *
 * @author jon
 *
 */
public class EdgePrimitivesRepositoryImpl extends AbstractEdgePrimitivesRepository {

    /**
     * Contructs a bew repo.
     */
    public EdgePrimitivesRepositoryImpl(EdgeTypes edgeTypes) {
        super(edgeTypes);
    }

    @Override
    protected TypedEdgePrimitivesRepositoryImpl createRepo(EdgeType edgeType) {
        return new TypedEdgePrimitivesRepositoryImpl(edgeType);
    }

}
