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

package org.jon.ivmark.graphit.core.graph.exception;

/**
 * This exception is thrown when an already existing entity is created.
 * 
 * @author jon
 * 
 */
public class DuplicateKeyException extends IllegalArgumentException {

    private static final long serialVersionUID = -6824895307499666274L;

    /**
     * Creates a new exception for the provided duplicated key.
     */
    public DuplicateKeyException(Object key) {
        super(String.format("Duplicate key encountered: %s", key.toString()));
    }

}
