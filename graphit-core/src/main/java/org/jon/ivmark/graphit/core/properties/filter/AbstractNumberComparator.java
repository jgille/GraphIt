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

public abstract class AbstractNumberComparator implements NumberComparator {

    @Override
    public int compareTo(Object other) {
        if (other instanceof Short) {
            return compareTo(((Short) other).shortValue());
        }
        if (other instanceof Integer) {
            return compareTo(((Integer) other).intValue());
        }
        if (other instanceof Long) {
            return compareTo(((Long) other).longValue());
        }
        if (other instanceof Double) {
            return compareTo(((Double) other).doubleValue());
        }
        if (other instanceof Float) {
            return compareTo(((Float) other).floatValue());
        }
        throw new IllegalArgumentException(String.format("Illegal class: %s for %s",
                other.getClass(), other));
    }

    protected int sign(float diff) {
        if (diff == 0) {
            return 0;
        } else if (diff < 0) {
            return -1;
        } else {
            return 1;
        }
    }

    protected int sign(double diff) {
        if (diff == 0) {
            return 0;
        } else if (diff < 0) {
            return -1;
        } else {
            return 1;
        }
    }

    protected int sign(int diff) {
        if (diff == 0) {
            return 0;
        } else if (diff < 0) {
            return -1;
        } else {
            return 1;
        }
    }

    protected int sign(long diff) {
        if (diff == 0) {
            return 0;
        } else if (diff < 0) {
            return -1;
        } else {
            return 1;
        }
    }

    protected int sign(short diff) {
        return diff;
    }

}
