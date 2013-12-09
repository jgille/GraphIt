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

import java.util.HashMap;
import java.util.Map;

public class PropertyFilterSettings extends HashMap<String, Map<String, Object>> {

    public PropertyFilterSettings appendFilter(String propertyName, String operator, Object target) {
        Map<String, Object> conditions = get(propertyName);
        if (conditions == null) {
            conditions = new HashMap<String, Object>();
            put(propertyName, conditions);
        }
        conditions.put(operator, target);
        return this;
    }
}
