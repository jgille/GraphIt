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

package org.graphit.performance;

import au.com.bytecode.opencsv.CSVReader;
import com.google.common.base.Preconditions;
import org.apache.commons.io.IOUtils;
import org.graphit.graph.service.PropertyGraph;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jon
 *
 */
public enum GraphMethodFactory {

    GET_EDGES {

        @Override
        protected GetEdgesMethod getMethod(PropertyGraph graph, GraphLoadTestStats stats) {
            return new GetEdgesMethod(graph, stats);
        }
    },
    GET_NEIGHBORS {

        @Override
        protected GetNeighborsMethod getMethod(PropertyGraph graph, GraphLoadTestStats stats) {
            return new GetNeighborsMethod(graph, stats);
        }

    },
    ADD_EDGE {
        @Override
        protected AddEdgeMethod getMethod(PropertyGraph graph, GraphLoadTestStats stats) {
            return new AddEdgeMethod(graph, stats);
        }
    },
    REMOVE_EDGE {
        @Override
        protected RemoveEdgeMethod getMethod(PropertyGraph graph, GraphLoadTestStats stats) {
            return new RemoveEdgeMethod(graph, stats);
        }
    };

    protected abstract GraphMethod<?> getMethod(PropertyGraph graph, GraphLoadTestStats stats);

    public static List<GraphMethod<?>> parseCsv(PropertyGraph graph, GraphLoadTestStats stats,
                                                File csvFile)
        throws IOException {
        CSVReader reader = null;
        List<GraphMethod<?>> methods = new ArrayList<GraphMethod<?>>();
        try {
            reader = new CSVReader(new FileReader(csvFile));
            String[] row;
            while ((row = reader.readNext()) != null) {
                methods.add(parseCsvRow(graph, stats, row));
            }
        } finally {
            IOUtils.closeQuietly(reader);
        }
        return methods;
    }

    private static GraphMethod<?> parseCsvRow(PropertyGraph graph, GraphLoadTestStats stats,
                                              String[] csvRow) {
        Preconditions.checkArgument(csvRow.length > 0);
        int i = 0;
        String factoryName = csvRow[i++];
        GraphMethodFactory factory = valueOf(factoryName);
        GraphMethod<?> method = factory.getMethod(graph, stats);
        String[] params = new String[csvRow.length - 1];
        System.arraycopy(csvRow, 1, params, 0, csvRow.length - 1);
        method.init(params);
        return method;
    }

}
