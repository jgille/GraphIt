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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.graphit.common.procedures.Mapper;
import org.graphit.common.procedures.Procedure;
import org.graphit.common.procedures.Reducer;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.TreeMultiset;

/**
 * An implementation of an {@link IterablePipe}.
 *
 * @author jon
 *
 */
public class IterablePipeImpl<E> implements IterablePipe<E> {

    private final Iterable<E> iterable;

    /**
     * Constructs a new empty instance.
     */
    public IterablePipeImpl() {
        this(Collections.<E> emptyList());
    }

    /**
     * Constructs a new instance containing the provided elements.
     */
    public IterablePipeImpl(E... elements) {
        this(Arrays.asList(elements));
    }

    /**
     * Constructs a new instance wrapping the provided iterable. Note that
     * modifications to the wrapped iterable will take effect here as well and
     * is not thread safe..
     */
    public IterablePipeImpl(Iterable<E> iterable) {
        this.iterable = iterable;
    }

    private static <E> IterablePipe<E> create(Iterable<E> iterable) {
        return new IterablePipeImpl<E>(iterable);
    }

    @Override
    public Iterator<E> iterator() {
        return iterable.iterator();
    }

    @Override
    public IterablePipe<E> head(int limit) {
        return create(Iterables.limit(iterable, limit));
    }

    @Override
    public IterablePipe<E> tail(int limit) {
        int skip = size() - limit;
        if (skip <= 0) {
            return this;
        }
        return skip(skip);
    }

    @Override
    public IterablePipe<E> skip(int skip) {
        return create(Iterables.skip(iterable, skip));
    }

    @Override
    public IterablePipe<E> filter(Predicate<E> filter) {
        return create(Iterables.filter(iterable, filter));
    }

    @Override
    public <T> IterablePipe<T> transform(Function<E, T> transformer) {
        return create(Iterables.transform(iterable, transformer));
    }

    @Override
    public IterablePipe<E> unique() {
        final Set<E> visited = new HashSet<E>();
        return filter(new Predicate<E>() {

            @Override
            public boolean apply(E element) {
                return visited.add(element);
            }
        });
    }

    @Override
    public <T> IterablePipe<T> map(Mapper<E, T> mapper) {
        return new IterablePipeImpl<T>(mapper.map(iterable));
    }

    @Override
    public <T> T reduce(Reducer<E, T> reducer) {
        return reducer.reduce(iterable);
    }

    @Override
    public <M, R> R mapReduce(Mapper<E, M> mapper, Reducer<M, R> reducer) {
        return map(mapper).reduce(reducer);
    }

    @Override
    public int size() {
        return Iterables.size(iterable);
    }

    @Override
    public boolean isEmpty() {
        return Iterables.isEmpty(iterable);
    }

    @Override
    public E get(int index) {
        return Iterables.get(iterable, index);
    }

    @Override
    public List<E> asList() {
        List<E> res = new ArrayList<E>();
        for (E element : iterable) {
            res.add(element);
        }
        return res;
    }

    @Override
    public Collection<E> asSortedCollection(Comparator<E> comparator) {
        TreeMultiset<E> res = TreeMultiset.create(comparator);
        for (E element : iterable) {
            res.add(element);
        }
        return res;
    }

    @Override
    public void forEach(Procedure<E> procedure) {
        for (E element : iterable) {
            if (!procedure.apply(element)) {
                break;
            }
        }
    }
}
