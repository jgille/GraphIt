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

package org.jon.ivmark.graphit.core;

/**
 *
 * Contains constants regarding concurrency, such as the default number of
 * partitions to use when splitting a repo for improved concurrency.
 *
 * @author jon
 *
 */
public final class ConcurrencyConstants {

    private ConcurrencyConstants() {}

    /**
     * The default number of shards/partitions to use when splitting some data
     * structure to improve concurrency.
     */
    public static final int DEFAULT_CONCURRENCY_LEVEL = Runtime.getRuntime().availableProcessors() + 1;

}
