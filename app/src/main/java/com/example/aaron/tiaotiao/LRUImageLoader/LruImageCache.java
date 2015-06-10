package com.example.aaron.tiaotiao.LRUImageLoader;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;


import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;

/**
 * Created by Aaron on 6/7/15.
 */
public class LruImageCache {
    /**
     * 从内存读取数据速度是最快的，为了更大限度使用内存，这里使用了两层缓存。
     * 硬引用缓存不会轻易被回收，用来保存常用数据，不常用的转入软引用缓存。
     */
    //软引用缓存容量
    private static final int SOFT_CACHE_SIZE = 15;
    //软引用缓存
    private static LinkedHashMap<String, SoftReference<Bitmap>> mSoftCache;
    //硬引用缓存
    private static LruCache<String, Bitmap> mLruCache;

    public LruImageCache() {
        //获得设备内存class （Return the approximate per-application memory class of the current device.）
//        int memClass = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
        int maxCache = (int) Runtime.getRuntime().maxMemory();
        //硬引用缓存大小，为设备内存25%
        int cacheSize = maxCache / 8;
        mLruCache = new LruCache<String, Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                if (null != value) {
                    //获得此 bitmap 大小
                    return value.getRowBytes() * value.getHeight();
                } else {
                    return 0;
                }
            }

            @Override
            protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
                if (null != oldValue) {
                    // 硬引用缓存容量满的时候，会根据LRU算法把最近没有被使用的图片转入此软引用缓存
                    mSoftCache.put(key, new SoftReference<Bitmap>(oldValue));
                }
            }
        };

        mSoftCache = new LinkedHashMap<String, SoftReference<Bitmap>>(SOFT_CACHE_SIZE, 0.75f, true){
            @Override
            protected boolean removeEldestEntry(Entry<String, SoftReference<Bitmap>> eldest) {
                //超出软引用大小，则根据 LRU 算法移除数据
                if (size() > SOFT_CACHE_SIZE) {
                    return true;
                } else {
                    return false;
                }
            }
        };
    }
    /**
     * 从RAM 中查找图片
     * */
    public Bitmap getBitmapFromCache(String imageUrl) {
        Bitmap bitmap;
        //先从硬引用中查找
        synchronized (mLruCache) {
            bitmap = mLruCache.get(imageUrl);
            if (null != bitmap) {
                //如果硬引用中找到 bitmap，则把 bitmap 取出重新放到 linkedhashmap 中，确保 LRU 算法最后删除它
                mLruCache.remove(imageUrl);
                mLruCache.put(imageUrl, bitmap);
                return bitmap;
            }
        }
        //再从软引用中查找
        synchronized (mSoftCache) {
            SoftReference<Bitmap> bitmapSoftReference = mSoftCache.get(imageUrl);
            if (null != bitmapSoftReference) {
                bitmap = bitmapSoftReference.get();
                if (null != bitmap) {
                    //把图片移回硬引用中
                    mLruCache.put(imageUrl, bitmap);
                    mSoftCache.remove(imageUrl);
                    return bitmap;
                } else {
                    //软引用中没有图片，则把这对键值从软引用中移除
                    mSoftCache.remove(imageUrl);
                }
            }
        }
        return null;
    }

    /**
     * 添加图片到 RAM
     */
    public void addBitmap2Cache(String imageUrl, Bitmap bitmap) {
        if (null != bitmap) {
            synchronized (mLruCache) {
                mLruCache.put(imageUrl, bitmap);
            }
        }
    }

    /**
     * 清空软引用
     * */
    public void clearCache() {
        mSoftCache.clear();
    }

}