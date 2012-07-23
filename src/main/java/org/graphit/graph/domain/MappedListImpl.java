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

package org.graphit.graph.domain;

import java.util.ArrayList;
import java.util.List;

import org.apache.mahout.math.map.AbstractObjectIntMap;
import org.apache.mahout.math.map.OpenObjectIntHashMap;
import org.graphit.graph.exception.DuplicateKeyException;
import org.springframework.util.Assert;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

/**
 * A simple {@link MappedList} backed by an {@link ArrayList} and an
 * {@link AbstractObjectIntMap}. All methods are thread safe.
 *
 * @author jon
 *
 */
public class MappedListImpl<E> implements MappedList<E> {

    private final AbstractObjectIntMap<E> indexMap;
    private final List<E> list;

    /**
     * Creates a new instance.
     */
    public MappedListImpl() {
        this.list = new ArrayList<E>();
        this.indexMap = new OpenObjectIntHashMap<E>();
    }

    @Override
    public E get(int index) {
        if (index < 0) {
            return null;
        }
        synchronized (list) {
            if (index >= list.size()) {
                return null;
            }
            return list.get(index);
        }
    }

    private void index(E element, int index) {
        if (element == null) {
            return;
        }
        synchronized (indexMap) {
            indexMap.put(element, index);
        }
    }

    @Override
    public int add(E element) {
        if (indexOf(element) != -1) {
            throw new DuplicateKeyException(element);
        }

        int index;
        synchronized (list) {
            index = list.size();
            list.add(element);
        }
        index(element, index);
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
        Assert.isTrue(index >= 0);
        synchronized (list) {
            for (int i = list.size(); i <= index; i++) {
                list.add(null);
            }
            list.set(index, element);
            index(element, index);
        }
    }

    @Override
    public E remove(int index) {
        Assert.isTrue(index >= 0);
        E element = get(index);
        if (element == null) {
            return null;
        }
        set(index, null);
        synchronized (indexMap) {
            indexMap.removeKey(element);
        }
        return element;
    }

    @Override
    public int indexOf(E element) {
        synchronized (indexMap) {
            if (indexMap.containsKey(element)) {
                return indexMap.get(element);
            }
            return -1;
        }
    }

    @Override
    public Iterable<E> iterable() {
        synchronized (list) {
            return Iterables.filter(new ArrayList<E>(list), new Predicate<E>() {

                @Override
                public boolean apply(E input) {
                    return input != null;
                }
            });
        }
    }
}
