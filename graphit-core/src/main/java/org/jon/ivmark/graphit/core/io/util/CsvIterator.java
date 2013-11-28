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

package org.jon.ivmark.graphit.core.io.util;

import java.io.Closeable;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.io.IOUtils;
import org.jon.ivmark.graphit.core.graph.traversal.Procedure;

import au.com.bytecode.opencsv.CSVReader;

/**
 * utility class for iterating a csv file.
 *
 * @author jon
 *
 */
public class CsvIterator implements Closeable {

    private final CSVReader reader;

    /**
     * Creates a new iterator.
     */
    public CsvIterator(CSVReader reader) {
        this.reader = reader;
    }

    /**
     * Creates an iterator for the given csv file and applies the procedure for
     * each row in the file.
     */
    public static void forEachRow(String file, char delimiter,
                                  char escapeChar, Procedure<CsvRow> procedure) throws IOException {
        CsvIterator iterator = null;
        try {
            iterator =
                new CsvIterator(new CSVReader(new InputStreamReader(new FileInputStream(file), "UTF8"),
                                              delimiter, escapeChar));
            iterator.forEachRow(procedure);
        } finally {
            IOUtils.closeQuietly(iterator);
        }
    }

    /**
     * Applies the procedure for each row in the csv file.
     */
    public void forEachRow(Procedure<CsvRow> procedure) throws IOException {
        String[] row;
        int rowNum = 1;
        while ((row = reader.readNext()) != null) {
            CsvRow csvRow = new CsvRow(rowNum++, row);
            if (!procedure.apply(csvRow)) {
                break;
            }
        }
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }

}
