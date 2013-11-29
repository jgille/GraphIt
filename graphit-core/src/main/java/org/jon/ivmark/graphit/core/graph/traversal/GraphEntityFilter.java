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

import com.google.common.base.Predicate;
import org.jon.ivmark.graphit.core.graph.entity.GraphEntity;
import org.jon.ivmark.graphit.core.properties.Properties;

public abstract class GraphEntityFilter<E extends GraphEntity> implements Predicate<E> {

    private Predicate<E> typeFilter;
    private Predicate<Properties> propertiesFilter;

    protected void setTypeFilter(Predicate<E> typeFilter) {
        this.typeFilter = typeFilter;
    }

    protected void setPropertiesFilter(Predicate<Properties> propertiesFilter) {
        this.propertiesFilter = propertiesFilter;
    }

    @Override
    public boolean apply(E entity) {
        if (typeFilter != null && !typeFilter.apply(entity)) {
            return false;
        } else if (propertiesFilter != null) {
            return propertiesFilter.apply(entity);
        } else {
            return true;
        }
    }
}
