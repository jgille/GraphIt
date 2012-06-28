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

import org.graphit.graph.edge.schema.EdgeType;
import org.graphit.graph.edge.util.EdgeIndexComparator;
import org.graphit.graph.edge.util.EdgeIndexComparatorImpl;
import org.graphit.graph.edge.util.EdgeWeightComparator;

public enum TestEdgeType implements EdgeType {

    SIMILAR {

        @Override
        public boolean isWeighted() {
            return true;
        }

        @Override
        public EdgeIndexComparator getEdgeComparator(EdgeWeigher weigher) {
            return new EdgeWeightComparator(weigher, true);
        }
    },
    BOUGHT {

        @Override
        public boolean isWeighted() {
            return false;
        }

        @Override
        public EdgeIndexComparator getEdgeComparator(EdgeWeigher ignored) {
            return new EdgeIndexComparatorImpl();
        }
    },
    VIEWED {

        @Override
        public boolean isWeighted() {
            return false;
        }

        @Override
        public EdgeIndexComparator getEdgeComparator(EdgeWeigher ignored) {
            return new EdgeIndexComparatorImpl();
        }
    };
    ;
}
