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

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.TreeMultiset;
import org.jon.ivmark.graphit.core.Procedure;

import java.util.*;

/**
 *
 * Use when traversing a graph.
 *
 * This class is immutable, all of the modifying methods returns a instance.
 *
 * @author jon
 *
 */
public class Traversable<E> implements Iterable<E> {

    private final Iterable<E> iterable;

    /**
     * Constructs a new empty instance.
     */
    public Traversable() {
        this(Collections.<E> emptyList());
    }

    /**
     * Constructs a new instance containing the provided elements.
     */
    public Traversable(E... elements) {
        this(Arrays.asList(elements));
    }

    /**
     * Constructs a new instance wrapping the provided iterable. Note that
     * modifications to the wrapped iterable will take effect here as well and
     * is not thread safe..
     */
    public Traversable(Iterable<E> iterable) {
        this.iterable = iterable;
    }

    private static <E> Traversable<E> create(Iterable<E> iterable) {
        return new Traversable<E>(iterable);
    }

    public Iterator<E> iterator() {
        return iterable.iterator();
    }

    /**
     * Returns a new instance only including the first 'limit' number of elements.
     */
    public Traversable<E> head(int limit) {
        return create(Iterables.limit(iterable, limit));
    }

    /**
     * Returns a new instance only including the last 'limit' number of elements.
     */
    public Traversable<E> tail(int limit) {
        int skip = size() - limit;
        if (skip <= 0) {
            return this;
        }
        return skip(skip);
    }

    /**
     * Returns a new instance discarding the first 'skip' elements.
     */
    public Traversable<E> skip(int skip) {
        return create(Iterables.skip(iterable, skip));
    }

    /**
     * Returns a new instance discarding all elements that does not match the filter.
     */
    public Traversable<E> filter(Predicate<E> filter) {
        return create(Iterables.filter(iterable, filter));
    }

    /**
     * Returns a new instance where all elements are transformed.
     */
    public <T> Traversable<T> transform(Function<E, T> transformer) {
        return create(Iterables.transform(iterable, transformer));
    }

    /**
     * Returns a new instance discarding all duplicate elements.
     */
    public Traversable<E> unique() {
        final Set<E> visited = new HashSet<E>();
        return filter(new Predicate<E>() {

            @Override
            public boolean apply(E element) {
                return visited.add(element);
            }
        });
    }

    /**
     * Maps to a new Traversable.
     */
    public <T> Traversable<T> map(Mapper<E, T> mapper) {
        return new Traversable<T>(mapper.map(iterable));
    }

    /**
     * Reduces this Traversable.
     */
    public <T> T reduce(Reducer<E, T> reducer) {
        return reducer.reduce(iterable);
    }

    /**
     * Performs a two step operation on this Traversable, first a map and then a reduce.
     */
    public <M, R> R mapReduce(Mapper<E, M> mapper, Reducer<M, R> reducer) {
        return map(mapper).reduce(reducer);
    }

    /**
     * Gets the size by traversing and counting all elements.
     */
    public int size() {
        return Iterables.size(iterable);
    }

    public boolean isEmpty() {
        return Iterables.isEmpty(iterable);
    }

    /**
     * Get the element at the specified position.
     */
    public E get(int index) {
        return Iterables.get(iterable, index);
    }

    public List<E> asList() {
        List<E> res = new ArrayList<E>();
        for (E element : iterable) {
            res.add(element);
        }
        return res;
    }

    public Collection<E> asSortedCollection(Comparator<E> comparator) {
        TreeMultiset<E> res = TreeMultiset.create(comparator);
        for (E element : iterable) {
            res.add(element);
        }
        return res;
    }

    /**
     * Applies a procedure for all elements.
     */
    public void forEach(Procedure<E> procedure) {
        for (E element : iterable) {
            if (!procedure.apply(element)) {
                break;
            }
        }
    }
}
