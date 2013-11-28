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

package org.jon.ivmark.graphit.core.graph.edge.schema;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jon.ivmark.graphit.core.graph.edge.util.UnsortedEdgeIndexComparator;
import org.junit.Test;

/**
 * @author jon
 * 
 */
public class EdgeTypeImplTest {

    @Test
    public void testGetDefaultEdgeIndexComparator() {
        EdgeTypeImpl edgeType = new EdgeTypeImpl("A");
        assertTrue(edgeType.getSortOrder().getEdgeComparator(null).getClass()
            == UnsortedEdgeIndexComparator.class);
    }

    @Test
    public void testName() {
        EdgeTypeImpl edgeType = new EdgeTypeImpl("A");
        assertEquals("A", edgeType.name());
    }

    @Test
    public void testEquals() {
        EdgeTypeImpl t1 = new EdgeTypeImpl("A");
        EdgeTypeImpl t2 = new EdgeTypeImpl("A");
        EdgeTypeImpl t3 = new EdgeTypeImpl("B");
        EdgeTypeImpl t4 = new EdgeTypeImpl(null);

        assertEquals(t1, t1);
        assertEquals(t1, t2);
        assertFalse(t1.equals(t3));
        assertFalse(t1.equals(t4));
        assertEquals(t4, t4);

        assertFalse(t1.equals(""));
        assertFalse(t1.equals(null));
    }

    @Test
    public void testHashCode() {
        EdgeTypeImpl t1 = new EdgeTypeImpl("A");
        EdgeTypeImpl t2 = new EdgeTypeImpl("A");
        EdgeTypeImpl t3 = new EdgeTypeImpl("B");
        EdgeTypeImpl t4 = new EdgeTypeImpl(null);

        assertEquals(t1.hashCode(), t1.hashCode());
        assertEquals(t1.hashCode(), t2.hashCode());
        assertFalse(t1.hashCode() == t3.hashCode());
        assertFalse(t1.hashCode() == t4.hashCode());
    }
}
