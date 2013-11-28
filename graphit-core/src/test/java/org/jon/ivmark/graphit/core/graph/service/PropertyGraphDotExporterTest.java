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

package org.jon.ivmark.graphit.core.graph.service;

import org.apache.commons.io.FileUtils;
import org.jon.ivmark.graphit.core.io.util.ResourceUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author jon
 *
 */
public class PropertyGraphDotExporterTest {

    @Rule
    public TemporaryFolder out = new TemporaryFolder();

    @Test
    public void testExport() throws IOException {
        PropertyGraph graph = new PropertyGraphImpl("test");
        File in = getResourceFile("exportedGraphNoProperties.json");
        graph.importJson(in);

        File outFile = out.newFile();
        new PropertyGraphDotExporter(graph).export(outFile);

        File expected = getResourceFile("expectedGraph.dot");

        String fileContent = FileUtils.readFileToString(outFile);
        assertNotNull(fileContent);
        assertEquals(FileUtils.readFileToString(expected), fileContent);
    }

    private File getResourceFile(String fileName) {
        String path = String.format("fixtures/%s", fileName);
        return ResourceUtils.resourceFile(path);
    }
}
