package com.example.aaron.tiaotiao.LRUImageLoader;

import android.app.Activity;
import android.graphics.Bitmap;
import android.widget.ImageView;

import java.util.concurrent.ExecutionException;

/**
 * Created by Aaron on 6/7/15.
 */
public class ImageLoader extends Activity{
    private LruImageCache lruImageCache = new LruImageCache();
    private ImageFileCache imageFileCache = new ImageFileCache();
//    private getImageFromHTTP imageFromHTTP = new getImageFromHTTP();
    //AsyncTask 被重复调用，因为 imageloader 只有一个实例，所以 getImageFromHTTP 也只有一个，在之前 asynctask 结束之前再次调用就会出错。
    public void displayImage(String imgUrl, ImageView imageView) {
        Bitmap bitmap = null;
        bitmap = lruImageCache.getBitmapFromCache(imgUrl);
        if (null == bitmap) {
            bitmap = imageFileCache.getImage(imgUrl);
            if (null == bitmap) {
                try {
                    //每次请求网络都新开辟一个 asynctask，性能低下。。。
                    GetImageFromHTTP imageFromHTTP = new GetImageFromHTTP();
                    bitmap = (Bitmap) imageFromHTTP.execute(imgUrl).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                if (null != bitmap) {
                    imageFileCache.saveBitmap2SD(imgUrl, bitmap);
                    lruImageCache.addBitmap2Cache(imgUrl, bitmap);
                }
            } else {
                lruImageCache.addBitmap2Cache(imgUrl, bitmap);
            }
        }

        imageView.setImageBitmap(bitmap);
    }

}
