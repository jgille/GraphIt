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

package org.jon.ivmark.graphit.core.properties.filter;

public class DoubleComparator extends AbstractNumberComparator {

    private final double doubleValue;

    public DoubleComparator(double value) {
        this.doubleValue = value;
    }

    @Override
    public int compareTo(short other) {
        return sign(doubleValue - other);
    }

    @Override
    public int compareTo(int other) {
        return sign(doubleValue - other);
    }

    @Override
    public int compareTo(long other) {
        return sign(doubleValue - other);
    }

    @Override
    public int compareTo(float other) {
        return sign(doubleValue - other);
    }

    @Override
    public int compareTo(double other) {
        return sign(doubleValue - other);
    }
}
