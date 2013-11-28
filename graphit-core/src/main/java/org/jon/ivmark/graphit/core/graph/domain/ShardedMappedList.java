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

package org.jon.ivmark.graphit.core.graph.domain;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import org.jon.ivmark.graphit.core.graph.exception.DuplicateKeyException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A {@link MappedList} that allows higher concurrency by using multiple backing
 * {@link MappedList}s. Throughput in a multithreaded environment should be
 * higher than for a {@link MappedListImpl} for all methods but
 * {@link #indexOf(Object)}.
 *
 * @author jon
 *
 */
public class ShardedMappedList<E> implements MappedList<E> {

    private final int concurrencyLevel;
    private final List<MappedList<E>> shards;
    private final AtomicInteger nextIndex = new AtomicInteger(-1);

    /**
     * Creates a new list split into <code>concurrencyLevel</code> number of
     * shards.
     */
    public ShardedMappedList(int concurrencyLevel) {
        this.concurrencyLevel = concurrencyLevel;
        this.shards = new ArrayList<MappedList<E>>(concurrencyLevel);
        for (int i = 0; i < concurrencyLevel; i++) {
            shards.add(new MappedListImpl<E>());
        }
    }

    private int getShardIndex(int index) {
        return Math.abs(index % concurrencyLevel);
    }

    private int getIndexInShard(int index) {
        return index / concurrencyLevel;
    }

    @Override
    public E get(int index) {
        int shardIndex = getShardIndex(index);
        int indexInShard = getIndexInShard(index);
        MappedList<E> shard = shards.get(shardIndex);
        return shard.get(indexInShard);
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
        int shardIndex = getShardIndex(index);
        int indexInShard = getIndexInShard(index);
        MappedList<E> shard = shards.get(shardIndex);
        shard.set(indexInShard, element);
    }

    @Override
    public E remove(int index) {
        int shardIndex = getShardIndex(index);
        int indexInShard = getIndexInShard(index);
        MappedList<E> shard = shards.get(shardIndex);
        return shard.remove(indexInShard);
    }

    @Override
    public int indexOf(E element) {
        int shardIndex = 0;
        for (MappedList<E> shard : shards) {
            int index = shard.indexOf(element);
            if (index >= 0) {
                return index * concurrencyLevel + shardIndex;
            }
            shardIndex++;
        }
        return -1;
    }

    @Override
    public Iterable<E> iterable() {
        List<Iterable<E>> iterables = new ArrayList<Iterable<E>>(concurrencyLevel);
        for (MappedList<E> shard : shards) {
            iterables.add(shard.iterable());
        }
        return Iterables.concat(iterables);
    }
}
