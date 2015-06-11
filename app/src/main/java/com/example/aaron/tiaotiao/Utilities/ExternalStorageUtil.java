package com.example.aaron.tiaotiao.Utilities;

import android.os.Environment;

/**
 * This Utility is created to get External Storage info
 * Created by Aaron on 6/11/15.
 */
public class ExternalStorageUtil {
    public ExternalStorageUtil() {
    }

    /**
     * @return SD card path
     * */
    public String getExternalStoragePath() {
        boolean isExisted;
        isExisted = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (isExisted) {
            return Environment.getExternalStorageDirectory().getPath();
        } else {
            return "";
        }
    }
}
