package org.opengraph.graph.repository;

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
        if (!file.exists()) {
            return;
        }
        repo.restore(file);
    }

}
