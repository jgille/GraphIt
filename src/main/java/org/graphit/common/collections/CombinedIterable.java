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
import java.util.Iterator;
import java.util.List;

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
public class CombinedIterable<T> implements Iterable<T> {

    private final List<Iterable<T>> iterables;

    /**
     * Constructs a new empty instance.
     */
    public CombinedIterable() {
        this(new ArrayList<Iterable<T>>());
    }

    /**
     * Constructs a new instance combining the provided iterables.
     */
    public CombinedIterable(List<Iterable<T>> iterables) {
        this.iterables = iterables;
    }

    /**
     * Adds a new member iterable.
     */
    public CombinedIterable<T> add(Iterable<T> iterable) {
        iterables.add(iterable);
        return this;
    }

    @Override
    public Iterator<T> iterator() {
        List<Iterator<T>> iterators = new ArrayList<Iterator<T>>(iterables.size());
        for (Iterable<T> it : iterables) {
            iterators.add(it.iterator());
        }
        return new CombinedIterator<T>(iterators);
    }

    @Override
    public String toString() {
        return "CombinedIterable [iterables=" + iterables + "]";
    }

    private static final class CombinedIterator<T> implements Iterator<T> {

        private final List<Iterator<T>> iterators;
        private int current = 0;

        /**
         * Creates a new iterator instance.
         */
        public CombinedIterator(List<Iterator<T>> iterators) {
            this.iterators = iterators;
        }

        @Override
        public boolean hasNext() {
            if (current >= iterators.size()) {
                return false;
            }
            Iterator<T> it = iterators.get(current);
            if (it.hasNext()) {
                return true;
            }
            current++;
            return hasNext();
        }

        @Override
        public T next() {
            Iterator<T> it = iterators.get(current);
            return it.next();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Remove is not supported.");
        }

    }
}
