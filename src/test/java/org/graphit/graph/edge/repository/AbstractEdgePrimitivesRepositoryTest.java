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

package org.graphit.graph.edge.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.graphit.graph.edge.schema.DynamicEdgeTypes;
import org.graphit.graph.edge.schema.EdgeType;
import org.graphit.graph.edge.schema.EdgeTypes;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * @author jon
 *
 */
public class AbstractEdgePrimitivesRepositoryTest {

    @Test
    public void testInit() {
        EdgeTypes edgeTypes = new DynamicEdgeTypes().add("foo");
        final TypedEdgePrimitivesRepository typedRepo = mock(TypedEdgePrimitivesRepository.class);
        AbstractEdgePrimitivesRepository repo = new AbstractEdgePrimitivesRepository(edgeTypes) {

            @Override
            protected TypedEdgePrimitivesRepository createRepo(EdgeType edgeType) {
                return typedRepo;
            }
        };

        final AtomicReference<String> dir = new AtomicReference<String>();
        final AtomicBoolean inited = new AtomicBoolean(false);
        doAnswer(new Answer<Void>() {

            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                dir.set((String) invocation.getArguments()[0]);
                return null;
            }
        }).when(typedRepo).setRootDataDirectory(anyString());

            doAnswer(new Answer<Void>() {

            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                inited.set(true);
                return null;
            }
        }).when(typedRepo).init();

        repo.setRootDataDirectory("someDir");
        repo.init();
        assertEquals("someDir", dir.get());
        assertTrue(inited.get());
    }

    @Test
    public void testShutdown() {
        EdgeTypes edgeTypes = new DynamicEdgeTypes().add("foo");
        final TypedEdgePrimitivesRepository typedRepo = mock(TypedEdgePrimitivesRepository.class);
        AbstractEdgePrimitivesRepository repo = new AbstractEdgePrimitivesRepository(edgeTypes) {

            @Override
            protected TypedEdgePrimitivesRepository createRepo(EdgeType edgeType) {
                return typedRepo;
            }
        };

        final AtomicReference<String> dir = new AtomicReference<String>();
        final AtomicBoolean shutdown = new AtomicBoolean(false);
        doAnswer(new Answer<Void>() {

            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                dir.set((String) invocation.getArguments()[0]);
                return null;
            }
        }).when(typedRepo).setRootDataDirectory(anyString());

        doAnswer(new Answer<Void>() {

            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                shutdown.set(true);
                return null;
            }
        }).when(typedRepo).shutdown();

        repo.setRootDataDirectory("someDir");
        repo.shutdown();
        assertEquals("someDir", dir.get());
        assertTrue(shutdown.get());
    }

}
