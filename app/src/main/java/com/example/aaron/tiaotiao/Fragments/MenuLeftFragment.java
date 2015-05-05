package com.example.aaron.tiaotiao.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aaron.tiaotiao.R;

/**
 * Created by Aaron on 5/2/15.
 */
public class MenuLeftFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.menuleft_layout, container, false);
    }
}
