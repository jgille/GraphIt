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

import org.graphit.common.enumeration.DynamicEnumerableElement;

/**
 * Describes an edge type in a graph.
 * 
 * @author jon
 * 
 */
public interface EdgeType extends DynamicEnumerableElement {

    /**
     * Defines how edges of this type are to be kept sorted.
     */
    EdgeSortOrder getSortOrder();

}
