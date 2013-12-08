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

public class NumberComparatorFactory {

    public NumberComparator comparatorFor(Object property) {
        if (property instanceof Short) {
            return new ShortComparator((Short) property);
        }
        if (property instanceof Integer) {
            return new IntComparator((Integer) property);
        }
        if (property instanceof Long) {
            return new LongComparator((Long) property);
        }
        if (property instanceof Double) {
            return new DoubleComparator((Double) property);
        }
        if (property instanceof Float) {
            return new FloatComparator((Float) property);
        }
        throw new IllegalArgumentException(String.format("Illegal class: %s for %s",
                property.getClass(), property));
    }

}
