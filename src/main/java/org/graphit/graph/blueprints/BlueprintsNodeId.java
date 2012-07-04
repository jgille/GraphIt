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

package org.graphit.graph.blueprints;

import org.graphit.graph.node.domain.NodeId;

/**
 * A blueprints compatible node id.
 *
 * @author jon
 *
 */
public class BlueprintsNodeId {

    private final NodeId wrappedNodeId;

    /**
     * Constructs a new id with the wrapped {@link NodeId}.
     */
    public BlueprintsNodeId(NodeId wrappedNodeId) {
        this.wrappedNodeId = wrappedNodeId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((wrappedNodeId == null) ? 0 : wrappedNodeId.hashCode());
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
        BlueprintsNodeId other = (BlueprintsNodeId) obj;
        if (wrappedNodeId == null) {
            if (other.wrappedNodeId != null) {
                return false;
            }
        } else if (!wrappedNodeId.equals(other.wrappedNodeId)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return wrappedNodeId.getId();
    }

}
