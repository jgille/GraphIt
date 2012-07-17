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

package org.graphit.common.procedures;

import java.util.Collection;

import org.apache.mahout.math.function.ObjectIntProcedure;
import org.apache.mahout.math.map.AbstractObjectIntMap;
import org.apache.mahout.math.map.OpenObjectIntHashMap;
import org.springframework.util.Assert;

/**
 * A {@link Counter} implementation backed by Mahout collections,
 * 
 * @author jon
 * 
 */
public class CounterImpl<E> implements Counter<E> {

    private final AbstractObjectIntMap<E> map;

    /**
     * Creates a new empty instance.
     */
    public CounterImpl() {
        this.map = new OpenObjectIntHashMap<E>();
    }

    @Override
    public int count(E element) {
        return map.get(element);
    }

    @Override
    public void add(E element) {
        map.adjustOrPutValue(element, 1, 1);
    }

    @Override
    public Iterable<CountedElement<E>> iterable(CountSortOrder sortOrder) {
        Assert.notNull(sortOrder);
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
