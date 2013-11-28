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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;

/**
 * Describes the sort order for iterating counted elements.
 *
 * @author jon
 */
public enum CountSortOrder {
    /**
     * Keeps insertion order when collection elements into a collection.
     */
    INSERTION_ORDER {
        @Override
        protected <E> Collection<CountedElement<E>> newCollection() {
            return new ArrayList<CountedElement<E>>();
        }
    },
    /**
     * Keeps elements sorted by ascending count.
     */
    ASCENDING_COUNT {
        @Override
        protected <E> Collection<CountedElement<E>> newCollection() {
            Comparator<CountedElement<E>> comparator =
                new Comparator<CountedElement<E>>() {

                    @Override
                    public int compare(CountedElement<E> e1, CountedElement<E> e2) {
                        return compareElements(e1, e2);
                    }
                };
            return new TreeSet<CountedElement<E>>(comparator);
        }
    },
    /**
     * Keeps elements sorted by descending count.
     */
    DESCENDING_COUNT {
        @Override
        protected <E> Collection<CountedElement<E>> newCollection() {
            Comparator<CountedElement<E>> comparator =
                new Comparator<CountedElement<E>>() {

                    @Override
                    public int compare(CountedElement<E> e1, CountedElement<E> e2) {
                        return compareElements(e2, e1);
                    }
                };
            return new TreeSet<CountedElement<E>>(comparator);
        }
    };

    /**
     * Gets an empty collection, possibly one that will be kept sorted.
     */
    protected abstract <E> Collection<CountedElement<E>> newCollection();

    /**
     * Compares two counted elements.
     */
    @SuppressWarnings("unchecked")
    protected <E> int compareElements(CountedElement<E> e1, CountedElement<E> e2) {
        int comp = e1.getCount() - e2.getCount();
        if (comp != 0) {
            return comp;
        }
        E elem1 = e1.getElement();
        if (elem1 instanceof Comparable) {
            return ((Comparable<E>) elem1).compareTo(e2.getElement());
        }
        return -1;
    }
}