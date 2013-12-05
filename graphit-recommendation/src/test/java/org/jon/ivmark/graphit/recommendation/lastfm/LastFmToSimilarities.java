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

package org.jon.ivmark.graphit.recommendation.lastfm;

import org.jon.ivmark.graphit.core.Procedure;
import org.jon.ivmark.graphit.core.io.util.CsvIterator;
import org.jon.ivmark.graphit.core.io.util.CsvRow;
import org.jon.ivmark.graphit.core.io.util.ResourceUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class LastFmToSimilarities {

    public static void main(String[] args) throws IOException {
        String file = ResourceUtils.resourceFile("lastfm/user_artists.dat").getAbsolutePath();

        String outFile = args[0];
        PrintWriter out = new PrintWriter(new FileWriter(outFile));
        try {
            processFile(file, out);
        } finally {
            out.close();
        }

    }

    private static void processFile(String in, final PrintWriter out) throws IOException {
        CsvIterator.forEachRow(in, '\t', '\\', new Procedure<CsvRow>() {

            @Override
            public boolean apply(CsvRow row) {
                int rowNum = row.getRowNum();
                if (rowNum == 1) {
                    return true; // Skip header
                }
                printListenedTo(row, out);
                return true;
            }
        });
    }

    private static void printListenedTo(CsvRow row, PrintWriter out) {
        int i = 0;
        String userId = row.getColumnData(i++);
        String artistId = row.getColumnData(i);
        out.write(String.format("%s,%s\n", userId, artistId));
    }
}
