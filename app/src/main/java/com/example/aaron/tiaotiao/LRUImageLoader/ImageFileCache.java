package com.example.aaron.tiaotiao.LRUImageLoader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by Aaron on 6/7/15.
 */
public class ImageFileCache {
    private static final String CACHE_DIR = "TiaoTiao";
    private static final String WHOLESALE_CONV = ".jpg";
    private static final int MB = 1024 * 1024;
    private static final int CACHE_SIZE = 10;
    private static final int FREE_SD_SPACE_NEEDED_TO_CACHE = 10;

    public ImageFileCache() {
    }
    /**
     * get bitmap from SD card
     * @return bitmap*/
    public Bitmap getImage(String imageUrl) {
        String imgPath = getCacheDirectory() + "/" + convertUrl2FileName(imageUrl);
        Log.d("decode image", imgPath);
        File imgFile = new File(imgPath);

        if (imgFile.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(imgPath, null);
            if (null == bitmap) {
                imgFile.delete();
            } else {
                updateFileTime(imgPath);
                return bitmap;
            }
        }
        return null;
    }

    public void saveBitmap2SD(String imgUrl, Bitmap bitmap) {
        if (null == bitmap) {
            return;
        }
        //后续需加上本地缓存的释放
        /*if (FREE_SD_SPACE_NEEDED_TO_CACHE > freeSapceOnSD()) {
            //not enough space on SD card
            removeCache(getCacheDirectory());
            return;
        }*/
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return;
        }
        try {
            String fileName = convertUrl2FileName(imgUrl);
            String dir = getCacheDirectory();
            File dirCache = new File(dir);
            File file = new File(dir + "/" + fileName);
            if (!dirCache.exists()) {
                if (dirCache.mkdir()) {
                    Log.d("EXTERNAL_STORAGE", "path created");
                } else {
                    Log.d("EXTERNAL_STORAGE", "path create failed");
                }
            }
            if (!file.exists()) {
                if (file.createNewFile()) {
                    Log.d("FILE LOG", "file created");
                } else {
                    Log.d("FILE LOG", "file create failed");
                }
            }

            //cache image res into file location
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bufferedOutputStream);
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
        } catch (IOException e) {

        }

    }

    /**
     * get each file's space
     * and delete the ones that bigger than CACHE_SIZE or
     * free space on SD is insufficient to further more cache
     */
    private boolean removeCache(String dirPath) {
        File dir = new File(dirPath);
        File[] files = dir.listFiles();
        if (null == files) {
            return true;
        }
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return false;
        }

        int dirSize = 0;
        for (int i = 0; i < files.length; ++i) {
            if (files[i].getName().contains(WHOLESALE_CONV)) {
                dirSize += files[i].length();
            }
        }

        if (dirSize > CACHE_SIZE * MB || FREE_SD_SPACE_NEEDED_TO_CACHE > freeSapceOnSD()) {
            int removeFactor = (int) ((0.4 * files.length) + 1);
            Arrays.sort(files, new FileLastModifiedSort());
            for (int i = 0; i < removeFactor; ++i) {
                files[i].getName().contains(WHOLESALE_CONV);
                files[i].delete();
            }
        }

        if (freeSapceOnSD() <= CACHE_SIZE) {
            return false;
        }

        return true;
    }
    /**
     * calculate free space on SD card
     * */
    public int freeSapceOnSD() {
        StatFs fs = new StatFs(Environment.getExternalStorageDirectory().getPath());
        double sdFreeMB = 0;
        if (Build.VERSION.SDK_INT <= 15) {
            sdFreeMB = ((double) fs.getBlockSize() * (double) fs.getBlockSize()) / MB;
        } else {
            sdFreeMB = ((double) fs.getAvailableBlocksLong() * (double) fs.getBlockSizeLong()) / MB;
        }
        return (int) sdFreeMB;

    }

    /**
     * 更新图片最近使用时间，以便 LRU 算法移除释放 SD 空间
     * */
    private void updateFileTime(String imgPath) {
        File file = new File(imgPath);
        long newModifiedTime = System.currentTimeMillis();
        file.setLastModified(newModifiedTime);
    }

    /**
     * 获取 URL 中的图片名称*/
    private String convertUrl2FileName(String imageUrl) {
        return imageUrl.substring(imageUrl.lastIndexOf("/") + 1, imageUrl.length());
    }

    /**
     * get SD card path
     * @return SD card path
     * */
    private String getSDcardPath() {
        String path;
        boolean SDexists = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (SDexists) {
            path = Environment.getExternalStorageDirectory().getPath();
            return path;
        } else {
            return "";
        }
    }

    /**
     * get image cache dir
     * @return directory
    * */
    private String getCacheDirectory() {
        return getSDcardPath() + "/" + CACHE_DIR;
    }

    private class FileLastModifiedSort implements Comparator<File> {

        @Override
        public int compare(File lhs, File rhs) {
            if (lhs.lastModified() > rhs.lastModified()) {
                return 1;
            } else if (lhs.lastModified() == rhs.lastModified()) {
                return 0;
            } else {
                return -1;
            }
        }
    }
}

