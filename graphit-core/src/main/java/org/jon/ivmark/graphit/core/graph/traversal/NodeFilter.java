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

import com.google.common.base.Predicate;
import org.jon.ivmark.graphit.core.graph.node.Node;
import org.jon.ivmark.graphit.core.graph.node.NodeType;
import org.jon.ivmark.graphit.core.properties.Properties;

public class NodeFilter implements Predicate<Node> {

    private NodeTypeFilter nodeTypeFilter;
    private Predicate<Properties> propertiesFilter;

    public NodeFilter withNodeTypes(NodeType... nodeTypes) {
        this.nodeTypeFilter = new NodeTypeFilter(nodeTypes);
        return this;
    }

    public NodeFilter filterOnProperties(Predicate<Properties> propertiesFilter) {
        this.propertiesFilter = propertiesFilter;
        return this;
    }

    @Override
    public boolean apply(Node node) {
        if (nodeTypeFilter != null && !nodeTypeFilter.apply(node)) {
            return false;
        } else if (propertiesFilter != null) {
            return propertiesFilter.apply(node);
        } else {
            return true;
        }
    }
}
