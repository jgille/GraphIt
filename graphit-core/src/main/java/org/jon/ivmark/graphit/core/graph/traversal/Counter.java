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

import com.google.common.base.Preconditions;
import org.apache.mahout.math.function.ObjectIntProcedure;
import org.apache.mahout.math.map.AbstractObjectIntMap;
import org.apache.mahout.math.map.OpenObjectIntHashMap;

import java.util.Collection;

/**
 *
 * Class for counting the number of times distinct elements occur in an
 * collection of elements.
 *
 * @author jon
 *
 */
public class Counter<E> {

    private final AbstractObjectIntMap<E> map;

    /**
     * Creates a new empty instance.
     */
    public Counter() {
        this.map = new OpenObjectIntHashMap<E>();
    }

    /**
     * Gets the number of times this element occurred.
     */
    public int count(E element) {
        return map.get(element);
    }

    /**
     * Adds an element.
     */
    public void add(E element) {
        map.adjustOrPutValue(element, 1, 1);
    }

    /**
     * Gets an iterable of all counted elements.
     */
    public Iterable<CountedElement<E>> iterable(CountSortOrder sortOrder) {
        Preconditions.checkNotNull(sortOrder);
        final Collection<CountedElement<E>> res = sortOrder.newCollection();

        map.forEachPair(new ObjectIntProcedure<E>() {

            @Override
            public boolean apply(E element, int count) {
                res.add(new CountedElement<E>(element, count));
                return true;
            }
        });
        return res;
    }
}
