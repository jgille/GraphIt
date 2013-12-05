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

import org.codehaus.jackson.map.ObjectMapper;
import org.jon.ivmark.graphit.core.Procedure;
import org.jon.ivmark.graphit.core.io.util.CsvIterator;
import org.jon.ivmark.graphit.core.io.util.CsvRow;
import org.jon.ivmark.graphit.core.io.util.ResourceUtils;
import org.jon.ivmark.graphit.recommendation.Item;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class LastFmToArtists {

    private static final Random RANDOM = new Random(1);

    private static final double[] PRICES = new double[] {10.99, 15.99, 16.99, 17.99, 20.99};

    public static void main(String[] args) throws IOException {
        String file = ResourceUtils.resourceFile("lastfm/artists.dat").getAbsolutePath();

        String outFile = args[0];
        List<Item> items = processFile(file);
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File(outFile), items);
    }

    private static Item importArtist(CsvRow row) {
        int i = 0;
        String id = row.getColumnData(i++);
        String name = row.getColumnData(i++);
        String url = row.getColumnData(i++);
        String pictureUrl = row.getColumnData(i);

        Map<String, Object> properties = new HashMap<String, Object>(5);
        properties.put("Name", name);
        properties.put("Url", url);
        properties.put("PictureUrl", pictureUrl);
        properties.put("Price", PRICES[Math.abs(RANDOM.nextInt()) % PRICES.length]);

        Item item = new Item(id, properties);
        return item;
    }

    private static List<Item> processFile(String in) throws IOException {
        final List<Item> artists = new ArrayList<Item>();
        CsvIterator.forEachRow(in, '\t', '\\', new Procedure<CsvRow>() {

            @Override
            public boolean apply(CsvRow row) {
                int rowNum = row.getRowNum();
                if (rowNum == 1) {
                    return true; // Skip header
                }
                Item artist = importArtist(row);
                artists.add(artist);
                return true;
            }
        });
        return artists;
    }

}
