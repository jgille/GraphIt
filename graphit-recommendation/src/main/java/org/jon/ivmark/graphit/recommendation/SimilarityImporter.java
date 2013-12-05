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

package org.jon.ivmark.graphit.recommendation;

import org.jon.ivmark.graphit.core.Procedure;
import org.jon.ivmark.graphit.core.io.util.CsvIterator;
import org.jon.ivmark.graphit.core.io.util.CsvRow;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class SimilarityImporter {

    private SimilarityImporter() {}

    public static List<Similarity> importSimilarities(File file, char delimiter) {
        final List<Similarity> similarities = new ArrayList<Similarity>();
        try {
            CsvIterator.forEachRow(file.getAbsolutePath(), delimiter, '\\', new Procedure<CsvRow>() {

                @Override
                public boolean apply(CsvRow row) {
                    int i = 0;
                    String source = row.getColumnData(i++);
                    String similar = row.getColumnData(i++);
                    float similarity = Float.valueOf(row.getColumnData(i));
                    similarities.add(new Similarity(source, similar, similarity));
                    return true;
                }
            });
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to import similarities", e);
        }
        return similarities;
    }
}
