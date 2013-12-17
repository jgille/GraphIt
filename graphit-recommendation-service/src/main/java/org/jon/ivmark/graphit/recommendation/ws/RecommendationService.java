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

package org.jon.ivmark.graphit.recommendation.ws;

import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import org.jon.ivmark.graphit.recommendation.repository.InMemoryRecommendationSettingsRepository;
import org.jon.ivmark.graphit.recommendation.repository.RecommendationSettingsRepository;
import org.jon.ivmark.graphit.recommendation.ws.config.RecommendationConfig;
import org.jon.ivmark.graphit.recommendation.ws.resource.RecommendationSettingsResource;

import static com.fasterxml.jackson.databind.PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES;

public class RecommendationService extends Service<RecommendationConfig> {

    private final RecommendationSettingsRepository settingsRepository;

    public RecommendationService(RecommendationSettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }

    @Override
    public void initialize(Bootstrap<RecommendationConfig> bootstrap) {
        bootstrap.setName("recommendation-service");
    }

    @Override
    public void run(RecommendationConfig recommendationConfig, Environment environment) {
        environment.getObjectMapperFactory().setPropertyNamingStrategy(CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);

        environment.addResource(new RecommendationSettingsResource(settingsRepository));
    }

    public static void main(String[] args) throws Exception {
        RecommendationSettingsRepository settingsRepository = new InMemoryRecommendationSettingsRepository();
        new RecommendationService(settingsRepository).run(args);
    }
}
