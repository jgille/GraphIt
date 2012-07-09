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

package org.graphit.graph.edge.util;

import org.graphit.graph.edge.schema.EdgeSortOrder;
import org.graphit.graph.edge.schema.EdgeType;

public enum TestEdgeType implements EdgeType {

    SIMILAR {

        @Override
        public EdgeSortOrder getSortOrder() {
            return EdgeSortOrder.DESCENDING_WEIGHT;
        }

    },
    BOUGHT {

        @Override
        public EdgeSortOrder getSortOrder() {
            return EdgeSortOrder.UNDEFINED;
        }

    },
    VIEWED {

        @Override
        public EdgeSortOrder getSortOrder() {
            return EdgeSortOrder.UNDEFINED;
        }
    };
}
