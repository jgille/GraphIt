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

package org.jon.ivmark.graphit.recommendation.repository;

import com.google.common.base.Preconditions;
import org.jon.ivmark.graphit.recommendation.CompositeRecommendationSettings;
import org.jon.ivmark.graphit.recommendation.Named;

import java.util.*;

public class InMemoryRecommendationSettingsRepository implements RecommendationSettingsRepository {

    private final Map<String, CompositeRecommendationSettings> storage;

    public InMemoryRecommendationSettingsRepository() {
        this.storage = Collections.synchronizedMap(new HashMap<String, CompositeRecommendationSettings>());
    }

    @Override
    public void save(CompositeRecommendationSettings recommendationSettings) {
        Preconditions.checkArgument(recommendationSettings.getId() != null);
        storage.put(recommendationSettings.getId(), recommendationSettings);
    }

    @Override
    public CompositeRecommendationSettings get(String id) {
        return storage.get(id);
    }

    @Override
    public List<Named> allSettings() {
        List<Named> all = new ArrayList<Named>();
        synchronized (storage) {
            for (Map.Entry<String, CompositeRecommendationSettings> e : storage.entrySet()) {
                all.add(new Named(e.getKey(), e.getValue().getName()));
            }
        }
        return all;
    }
}
