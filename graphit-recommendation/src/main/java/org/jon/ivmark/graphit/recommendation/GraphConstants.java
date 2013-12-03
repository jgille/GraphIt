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

package org.jon.ivmark.graphit.recommendation;

import org.jon.ivmark.graphit.core.graph.edge.EdgeType;
import org.jon.ivmark.graphit.core.graph.node.NodeType;

import static org.jon.ivmark.graphit.core.graph.edge.EdgeSortOrder.DESCENDING_WEIGHT;

public final class GraphConstants {

    private GraphConstants() {}

    public static final EdgeType OTHERS_ALSO_BOUGHT = new EdgeType("OthersAlsoBought", DESCENDING_WEIGHT);
    public static final EdgeType OTHERS_ALSO_VIEWED = new EdgeType("OthersAlsoViewed", DESCENDING_WEIGHT);
    public static final EdgeType OTHERS_ALSO_LIKED = new EdgeType("OthersAlsoLiked", DESCENDING_WEIGHT);

    public static final NodeType ITEM = new NodeType("Item");

}
