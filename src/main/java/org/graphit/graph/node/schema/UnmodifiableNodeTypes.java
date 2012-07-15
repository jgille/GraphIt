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

package org.graphit.graph.node.schema;

/**
 * Base class for unmodifiable {@link NodeTypes}.
 *
 * @author jon
 *
 */
public abstract class UnmodifiableNodeTypes implements NodeTypes {

    @Override
    public void add(NodeType nodeType) {
        throw new UnsupportedOperationException("This instance is unmodifiable.");
    }

    @Override
    public void add(String nodeTypeName) {
        throw new UnsupportedOperationException("This instance is unmodifiable.");
    }

    @Override
    public NodeType getOrAdd(String nodeTypeName) {
        return valueOf(nodeTypeName);
    }
}
