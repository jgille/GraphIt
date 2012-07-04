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

package org.graphit.common.collections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Iterables;

/**
 * An {@link Iterable} that combines a collection of {@link Iterable}s, i.e. [1,
 * 2, 3] + [4, 5] -> [1, 2, 3, 4, 5].
 *
 * This class is not thread safe.
 *
 * @author jon
 *
 * @param <T>
 *            The generic type of the elements in the {@link Iterable}.
 */
public class CombinedIterables<T> {

    private final List<Iterable<T>> iterables;

    /**
     * Constructs a new empty instance.
     */
    public CombinedIterables() {
        this(new ArrayList<Iterable<T>>());
    }

    /**
     * Constructs a new instance combining the provided iterables.
     */
    public CombinedIterables(List<Iterable<T>> iterables) {
        this.iterables = iterables;
    }

    /**
     * Adds a new member iterable.
     */
    public CombinedIterables<T> add(Iterable<T> iterable) {
        iterables.add(iterable);
        return this;
    }

    public Iterable<T> iterable() {
        if (iterables.isEmpty()) {
            return Collections.emptyList();
        }
        if (iterables.size() == 1) {
            return iterables.get(0);
        }
        return Iterables.concat(iterables);
    }

    @Override
    public String toString() {
        return "CombinedIterable [iterables=" + iterables + "]";
    }
}
