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

package org.jon.ivmark.graphit.tinkerpop.examples.tinkerpop;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.gremlin.java.GremlinPipeline;
import org.jon.ivmark.graphit.core.graph.node.domain.NodeId;
import org.jon.ivmark.graphit.core.graph.service.PropertyGraph;
import org.jon.ivmark.graphit.core.graph.service.PropertyGraphImpl;
import org.jon.ivmark.graphit.core.io.util.ResourceUtils;
import org.jon.ivmark.graphit.tinkerpop.blueprints.BlueprintsGraph;

import java.io.IOException;
import java.util.List;

import static org.jon.ivmark.graphit.core.examples.ExampleConstants.*;

/**
 * Some examples using Gremlin pipes.
 *
 * @author jon
 *
 */
public class GremlinPipesExamples {

    private static PropertyGraph importPropertyGraph() throws IOException {
        PropertyGraph graph = new PropertyGraphImpl();
        graph.importJson(ResourceUtils.resourceFile("/examples/musicstore.json"));
        return graph;
    }

    private List<Object> getJohnsPurchases(Graph graph) throws IOException {
        GremlinPipeline<Vertex, Vertex> pipe = new GremlinPipeline<Vertex, Vertex>();
        NodeId john = new NodeId(USER, "john.doe");
        return pipe.start(graph.getVertex(john))
            .out(BOUGHT.name())
            .property("Title").toList();
    }

    private List<Object> getUsersThatListendedToTracksJohnBought(Graph graph) throws IOException {
        GremlinPipeline<Vertex, Vertex> pipe = new GremlinPipeline<Vertex, Vertex>();
        NodeId john = new NodeId(USER, "john.doe");
        return pipe.start(graph.getVertex(john))
            .out(BOUGHT.name())
            .in(LISTENED_TO.name())
            .dedup()
            .property("Name")
            .toList();
    }

    /**
     * Main method that runs through the examples.
     */
    public static void main(String[] args) throws IOException {
        GremlinPipesExamples examples = new GremlinPipesExamples();
        PropertyGraph propertyGraph = importPropertyGraph();
        Graph graph = new BlueprintsGraph(propertyGraph);
        System.out.println("John's purhcases: " + examples.getJohnsPurchases(graph));
        System.out.println("Other users: "
            + examples.getUsersThatListendedToTracksJohnBought(graph));
    }
}
