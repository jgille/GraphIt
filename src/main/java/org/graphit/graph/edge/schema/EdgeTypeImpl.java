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

package org.graphit.graph.edge.schema;

import org.graphit.graph.edge.util.EdgeIndexComparator;
import org.graphit.graph.edge.util.UnsortedEdgeIndexComparator;
import org.graphit.graph.schema.AbstractGraphType;

/**
 * Basic {@link EdgeType} implementation. Defaults to being unweighted and
 * unsorted.
 *
 * @author jon
 *
 */
public class EdgeTypeImpl extends AbstractGraphType implements EdgeType {

    private final boolean isWeighted;
    private EdgeIndexComparator edgeComparator = new UnsortedEdgeIndexComparator();

    /**
     * Creates an unweighted edge type.
     */
    public EdgeTypeImpl(String name) {
        this(name, false);
    }

    /**
     * Creates a weighted or unweighted edge type.
     */
    public EdgeTypeImpl(String name, boolean isWeighted) {
        super(name);
        this.isWeighted = isWeighted;
    }

    public void setEdgeComparator(EdgeIndexComparator edgeComparator) {
        this.edgeComparator = edgeComparator;
    }

    @Override
    public boolean isWeighted() {
        return isWeighted;
    }

    @Override
    public EdgeIndexComparator getEdgeComparator() {
        return edgeComparator;
    }

    @Override
    public String toString() {
        return "EdgeTypeImpl [name=" + name() + ", isWeighted=" + isWeighted
            + ", edgeComparator="
            + edgeComparator + "]";
    }

}
