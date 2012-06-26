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

/**
 * Basic {@link EdgeType} implementation. Defaults to being unweighted and
 * unsorted.
 *
 * @author jon
 *
 */
public class EdgeTypeImpl implements EdgeType {

    private final String name;
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
        this.name = name;
        this.isWeighted = isWeighted;
    }

    public void setEdgeComparator(EdgeIndexComparator edgeComparator) {
        this.edgeComparator = edgeComparator;
    }

    @Override
    public String name() {
        return name;
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
        return "EdgeTypeImpl [name=" + name + ", isWeighted=" + isWeighted + ", edgeComparator="
            + edgeComparator + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        EdgeTypeImpl other = (EdgeTypeImpl) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }

}
