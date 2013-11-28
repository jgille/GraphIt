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

package org.jon.ivmark.graphit.core.graph.utils;

import java.util.Arrays;
import java.util.List;

/**
 * Represents a row in a csv file.
 *
 * @author jon
 *
 */
public class CsvRow {

    private final int rowNum;
    private final List<String> row;

    /**
     * Creates a new instance.
     */
    protected CsvRow(int rowNum, String[] row) {
        this.rowNum = rowNum;
        this.row = Arrays.asList(row);
    }

    /**
     * Gets the row number.
     */
    public int getRowNum() {
        return rowNum;
    }

    /**
     * Gets the number of columns for this row.
     */
    public int getColumnCount() {
        return row.size();
    }

    /**
     * Gets the column with the given column number for this row.
     */
    public String getColumnData(int colNum) {
        return row.get(colNum);
    }
}
