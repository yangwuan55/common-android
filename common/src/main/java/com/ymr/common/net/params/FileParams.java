package com.ymr.common.net.params;

import java.io.File;
import java.util.Map;

/**
 * Created by ymr on 15/8/21.
 */
public abstract class FileParams extends SimpleNetParams {

    private final Map<File, String> mFileMap;

    public FileParams(String tailUrl,Map<File,String> fileMap) {
        super(tailUrl);
        mFileMap = fileMap;
    }

    public Map<File,String> getFileMap() {
        return mFileMap;
    }
}
