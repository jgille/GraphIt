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

package org.graphit.graph.edge.util;

import java.io.Serializable;

/**
 * An {@link EdgeIndexComparator} to use when edges are not to be kept sorted.
 *
 * @author jon
 *
 */
public class UnsortedEdgeIndexComparator implements EdgeIndexComparator, Serializable {

    private static final long serialVersionUID = 8560010060998738359L;

    @Override
    public int compare(Integer o1, Integer o2) {
        return -1;
    }

    @Override
    public boolean isSorted() {
        return false;
    }

}
