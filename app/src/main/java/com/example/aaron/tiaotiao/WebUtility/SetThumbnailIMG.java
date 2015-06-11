package com.example.aaron.tiaotiao.WebUtility;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Aaron on 5/24/15.
 */
public class SetThumbnailIMG extends AsyncTask {
    Bitmap bitmap = null;
    ImageView imageView = null;
    @Override
    protected Object doInBackground(Object[] params) {
        imageView = (ImageView) params[0];
        String url = (String) params[1];

        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) (new URL(url)).openConnection();
            bitmap = BitmapFactory.decodeStream(httpURLConnection.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        imageView.setImageBitmap(bitmap);
    }
}
