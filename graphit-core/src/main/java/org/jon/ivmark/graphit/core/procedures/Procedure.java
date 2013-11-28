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

package org.jon.ivmark.graphit.core.procedures;

/**
 * A generically typed procedure.
 * 
 * @author jon
 * 
 * @param <T>
 *            The generic type of the objects this procedure may be applied for,
 */
public interface Procedure<T> {

    /**
     * Applies the procedure.
     * 
     * Returns a flag that among other things can be used to decide whether or
     * not to continue a forEach-loop or not.
     * 
     */
    boolean apply(T element);

}
