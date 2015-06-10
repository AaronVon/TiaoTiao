package com.example.aaron.tiaotiao.LRUImageLoader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Aaron on 6/7/15.
 */
public class GetImageFromHTTP extends AsyncTask{
    @Override
    protected Object doInBackground(Object[] params) {

        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(new URL(params[0].toString()).openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
