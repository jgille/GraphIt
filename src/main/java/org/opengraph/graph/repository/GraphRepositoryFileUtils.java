package org.opengraph.graph.repository;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.util.StringUtils;

public class GraphRepositoryFileUtils {

    public static void persist(GraphRepository repo, String dir, String fileName)
        throws IOException {
        if (!StringUtils.hasText(dir)) {
            return;
        }
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
            throw new RuntimeException(String.format("Failed to version %s.", fileName));
        }

        if (!stageFile.renameTo(master)) {
            throw new RuntimeException("Failed to make new file master.");
        }

        FileUtils.forceDelete(stageDir);
    }

}
