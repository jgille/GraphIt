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

package org.jon.ivmark.graphit.core.graph.traversal;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.google.common.base.Predicate;
import org.jon.ivmark.graphit.core.graph.node.Node;
import org.jon.ivmark.graphit.core.graph.node.NodeType;

/**
 * A filter on node type.
 *
 * @author jon
 *
 */
public class NodeTypeFilter implements Predicate<Node> {

    private final Set<NodeType> nodeTypes;

    /**
     * Creates a new filter.
     */
    public NodeTypeFilter(NodeType... nodeTypes) {
        this.nodeTypes = new HashSet<NodeType>(Arrays.asList(nodeTypes));
    }

    @Override
    public boolean apply(Node node) {
        return nodeTypes.contains(node.getType());
    }

}
