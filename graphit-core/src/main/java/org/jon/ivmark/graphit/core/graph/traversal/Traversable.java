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
import org.jon.ivmark.graphit.core.Procedure;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 *
 * Use when traversing a graph.
 *
 * Note that an {@link Traversable} is always immutable, all of the methods
 * returns a instance.
 *
 * @author jon
 *
 */
public interface Traversable<E> extends Iterable<E> {

    /**
     * Creates a new iterable pipe with the first limit elements of this
     * iterable pipe.
     */
    Traversable<E> head(int limit);

    /**
     * Creates a new iterable pipe with the last limit elements of this iterable
     * pipe.
     *
     * Note that this will iterate the entire pipe to find the number of
     * elements to skip.
     */
    Traversable<E> tail(int limit);

    /**
     * Creates a new iterable pipe which skips the first skip elements of this
     * iterable pipe.
     */
    Traversable<E> skip(int skip);

    /**
     * Creates a new iterable pipe containing only the elements of this iterable
     * pipe that matches the filter.
     */
    Traversable<E> filter(Predicate<E> filter);

    /**
     * Creates a new iterable pipe containing the elements of this iterable pipe
     * transformed to something else.
     */
    <T> Traversable<T> transform(Function<E, T> transformer);

    /**
     * Creates a new iterable pipe containing only unique elements.
     */
    Traversable<E> unique();

    /**
     * Performs a map function on the pipe, transforming it to another pipe.
     */
    <T> Traversable<T> map(Mapper<E, T> mapper);

    /**
     * Performs a reduce function on the pipe, transforming it to something
     * else.
     */
    <T> T reduce(Reducer<E, T> reducer);

    /**
     * Performs a two step operation on this iterable. First it is mapped to
     * another iterable, then it is transformed into a final result.
     */
    <M, R> R mapReduce(Mapper<E, M> mapper,
                       Reducer<M, R> reducer);

    /**
     * Gets the number of elements in this iterable pipe.
     */
    int size();

    /**
     * Returns true if there are no elements in this iterable pipe.
     */
    boolean isEmpty();

    /**
     * Applies a procedure for each element in the pipe, stopping if a procedure
     * call return false.
     */
    void forEach(Procedure<E> procedure);

    /**
     * Gets the element at index in this iterable pipe.
     */
    E get(int index);

    /**
     * Returns this iterable pipe as a list.
     */
    List<E> asList();

    /**
     * Returns the elements in this iterable pipe sorted according to the
     * comparator.
     */
    Collection<E> asSortedCollection(Comparator<E> comparator);
}
