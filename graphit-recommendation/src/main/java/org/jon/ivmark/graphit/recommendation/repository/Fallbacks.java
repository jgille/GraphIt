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

import org.jon.ivmark.graphit.recommendation.Fallback;

import java.util.Map;

public class Fallbacks {

    private final Map<String, Fallback> fallbacks;

    public Fallbacks(Map<String, Fallback> fallbacks) {
        this.fallbacks = fallbacks;
    }

    public Fallback fallback(String fallbackId) {
        Fallback fallback = fallbacks.get(fallbackId);
        if (fallback == null) {
            throw new IllegalArgumentException("No such fallback: " + fallbackId);
        }
        return fallback;
    }
}