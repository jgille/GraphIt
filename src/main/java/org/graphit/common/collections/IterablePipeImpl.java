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
import java.util.Iterator;
import java.util.List;

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
    public IterablePipe<E> limit(int limit) {
        return create(Iterables.limit(iterable, limit));
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
    public IterablePipe<E> append(Iterable<E> other) {
        return create(Iterables.concat(iterable, other));
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
}
