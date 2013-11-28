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

package org.jon.ivmark.graphit.examples;

import org.jon.ivmark.graphit.core.graph.edge.EdgeSortOrder;
import org.jon.ivmark.graphit.core.graph.edge.EdgeType;
import org.jon.ivmark.graphit.core.graph.edge.EdgeTypeImpl;
import org.jon.ivmark.graphit.core.graph.node.NodeType;
import org.jon.ivmark.graphit.core.graph.node.NodeTypeImpl;

/**
 * Some constants used by the examples classes.
 * 
 * @author jon
 * 
 */
public class ExampleConstants {

    public static final NodeType USER = new NodeTypeImpl("User");
    public static final NodeType TRACK = new NodeTypeImpl("Track");
    public static final EdgeType BOUGHT = new EdgeTypeImpl("Bought");
    public static final EdgeType LISTENED_TO = new EdgeTypeImpl("ListenedTo");
    public static final EdgeType SIMILAR = new EdgeTypeImpl("Similar", EdgeSortOrder.DESCENDING_WEIGHT);

}
