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

package org.jon.ivmark.graphit.core.graph.node.repository;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import org.jon.ivmark.graphit.core.graph.exception.DuplicateKeyException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A {@link IndexedList} that allows higher concurrency by using multiple backing
 * {@link IndexedList}s. Throughput in a multithreaded environment should be
 * higher than for a {@link IndexedListImpl} for all methods but
 * {@link #indexOf(Object)}.
 *
 * @author jon
 *
 */
public class ConcurrentIndexedList<E> implements IndexedList<E> {

    private final int concurrencyLevel;
    private final List<IndexedList<E>> segments;
    private final AtomicInteger nextIndex = new AtomicInteger(-1);

    /**
     * Creates a new list split into <code>concurrencyLevel</code> number of
     * segments
     */
    public ConcurrentIndexedList(int concurrencyLevel) {
        this.concurrencyLevel = concurrencyLevel;
        this.segments = new ArrayList<IndexedList<E>>(concurrencyLevel);
        for (int i = 0; i < concurrencyLevel; i++) {
            segments.add(new IndexedListImpl<E>());
        }
    }

    private int getSegmentIndex(int index) {
        return Math.abs(index % concurrencyLevel);
    }

    private int getIndexInSegment(int index) {
        return index / concurrencyLevel;
    }

    @Override
    public E get(int index) {
        int segmentIndex = getSegmentIndex(index);
        int indexInSegment = getIndexInSegment(index);
        IndexedList<E> segment = segments.get(segmentIndex);
        return segment.get(indexInSegment);
    }

    @Override
    public int add(E element) {
        int index = nextIndex.incrementAndGet();
        insert(index, element);
        return index;
    }

    @Override
    public void insert(int index, E element) {
        if (indexOf(element) != -1) {
            throw new DuplicateKeyException(element);
        }
        set(index, element);
    }

    @Override
    public void set(int index, E element) {
        Preconditions.checkArgument(index >= 0);
        int segmentIndex = getSegmentIndex(index);
        int indexInSegment = getIndexInSegment(index);
        IndexedList<E> segment = segments.get(segmentIndex);
        segment.set(indexInSegment, element);
    }

    @Override
    public E remove(int index) {
        int segmentIndex = getSegmentIndex(index);
        int indexInSegment = getIndexInSegment(index);
        IndexedList<E> segment = segments.get(segmentIndex);
        return segment.remove(indexInSegment);
    }

    @Override
    public int indexOf(E element) {
        int segmentIndex = 0;
        for (IndexedList<E> segment : segments) {
            int index = segment.indexOf(element);
            if (index >= 0) {
                return index * concurrencyLevel + segmentIndex;
            }
            segmentIndex++;
        }
        return -1;
    }

    @Override
    public Iterable<E> iterable() {
        List<Iterable<E>> iterables = new ArrayList<Iterable<E>>(concurrencyLevel);
        for (IndexedList<E> segment : segments) {
            iterables.add(segment.iterable());
        }
        return Iterables.concat(iterables);
    }
}
