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

package org.graphit.graph.node.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import org.graphit.graph.node.schema.NodeType;
import org.graphit.graph.node.schema.NodeTypes;

public class TestNodeTypes implements NodeTypes {

    private final List<NodeType> elements;

    public TestNodeTypes() {
        this.elements = new ArrayList<NodeType>();
        for (TestNodeType nt : EnumSet.allOf(TestNodeType.class)) {
            elements.add(nt);
        }
    }

    @Override
    public NodeType valueOf(String name) {
        return TestNodeType.valueOf(name);
    }

    @Override
    public Collection<NodeType> elements() {
        return Collections.unmodifiableList(elements);
    }

    @Override
    public int size() {
        return elements.size();
    }
}
