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

package org.graphit.graph.repository;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * Utility methods for reading/writing graph repositories from/to disk.
 *
 * @author jon
 *
 */
public final class GraphRepositoryFileUtils {

    private GraphRepositoryFileUtils() {
    }

    /**
     * Dumps the repo to disk. This method will first dump the repo to a
     * temporary stage file. If this goes ok, the master file (if any) is
     * renamed and moved to a versions directory. The stage file is then renamed
     * to be the new master.
     */
    public static void persist(GraphRepository repo, String dir, String fileName)
        throws IOException {
        if (!StringUtils.hasText(dir)) {
            return;
        }

        Assert.isTrue(StringUtils.hasText(fileName));

        File stageDir = new File(FilenameUtils.concat(dir, "stage"));
        FileUtils.forceMkdir(stageDir);
        String stageFileName = String.format("%s.%d", fileName, System.currentTimeMillis());
        File stageFile = new File(stageDir, stageFileName);
        repo.dump(stageFile);

        File master = new File(FilenameUtils.concat(dir, fileName));

        File versionDir = new File(FilenameUtils.concat(dir, "versions"));
        FileUtils.forceMkdir(versionDir);
        if (master.exists()
            && !master.renameTo(new File(versionDir, String.format("%s.%d",
                                                                   fileName,
                                                                   System.currentTimeMillis())))) {
            throw new IOException(String.format("Failed to version %s.", fileName));
        }

        if (!stageFile.renameTo(master)) {
            throw new IOException("Failed to make new file master.");
        }

        FileUtils.forceDelete(stageDir);
    }

    /**
     * Restores a graph repo from file.
     */
    public static void restore(GraphRepository repo, String dir, String fileName)
        throws IOException {
        if (!StringUtils.hasText(dir)) {
            return;
        }
        File file = new File(FilenameUtils.concat(dir, fileName));
        repo.restore(file);
    }

}
