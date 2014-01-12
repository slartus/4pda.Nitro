package org.pda.common;

import java.io.File;

/**
 * Created by slartus on 12.01.14.
 */
public class FileExternals {
    public static Boolean mkDirs(String filePath) {
        //int startind=1;
        String dirPath = new File(filePath).getParentFile().getAbsolutePath() + File.separator;

        File dir = new File(dirPath.replace("/", File.separator));
        return dir.exists() || dir.mkdirs();


    }
}
