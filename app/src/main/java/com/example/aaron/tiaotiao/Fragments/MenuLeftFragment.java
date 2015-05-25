package com.example.aaron.tiaotiao.Fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.aaron.tiaotiao.R;
import com.example.aaron.tiaotiao.WebImage.toRoundImg;

import java.util.concurrent.ExecutionException;

/**
 * Created by Aaron on 5/2/15.
 */
public class MenuLeftFragment extends Fragment {
    ImageView userImageView = null;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.menuleft_layout, container, false);
        userImageView = (ImageView) rootView.findViewById(R.id.userPic);

        initUserImg();

        return rootView;
    }

    private void initUserImg() {
        toRoundImg roundImg = new toRoundImg("http://www.eoeandroid.com/uc_server/avatar.php?uid=1112756&size=middle");
        try {
            Bitmap bitmap = (Bitmap) roundImg.execute().get();
            if (bitmap == null) {
                Log.d("bitmap errorrrrrr", "rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr");
            }
            userImageView.setImageBitmap(bitmap);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }
}
