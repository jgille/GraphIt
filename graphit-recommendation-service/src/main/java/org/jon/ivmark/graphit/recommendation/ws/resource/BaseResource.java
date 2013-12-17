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

package org.jon.ivmark.graphit.recommendation.ws.resource;

import org.jon.ivmark.graphit.core.Json;

import javax.ws.rs.WebApplicationException;
import java.io.IOException;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

public abstract class BaseResource {

    protected <T> T unmarshal(String json, Class<T> cls) {
        try {
            return Json.OBJECT_MAPPER.readValue(json, cls);
        } catch (IOException e) {
            throw new WebApplicationException(e, BAD_REQUEST);
        }
    }

    protected String marshal(Object value) {
        try {
            return Json.OBJECT_MAPPER.writeValueAsString(value);
        } catch (IOException e) {
            throw new WebApplicationException(e, INTERNAL_SERVER_ERROR);
        }
    }
}
