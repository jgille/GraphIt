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

package org.jon.ivmark.graphit.core.graph.edge.domain;

import org.jon.ivmark.graphit.core.graph.edge.schema.EdgeSortOrder;
import org.jon.ivmark.graphit.core.graph.edge.schema.EdgeType;
import org.jon.ivmark.graphit.core.graph.edge.schema.EdgeTypeImpl;
import org.jon.ivmark.graphit.core.graph.edge.schema.EdgeTypes;

public class TestEdgeTypes {

    public static final EdgeType SIMILAR = new EdgeTypeImpl("SIMILAR",
                                                            EdgeSortOrder.DESCENDING_WEIGHT);
    public static final EdgeType BOUGHT = new EdgeTypeImpl("BOUGHT");
    public static final EdgeType VIEWED = new EdgeTypeImpl("VIEWED");

    public static EdgeTypes getEdgeTypes() {
        EdgeTypes edgeTypes = new EdgeTypes();
        edgeTypes.add(SIMILAR);
        edgeTypes.add(BOUGHT);
        edgeTypes.add(VIEWED);
        return edgeTypes;
    }

}