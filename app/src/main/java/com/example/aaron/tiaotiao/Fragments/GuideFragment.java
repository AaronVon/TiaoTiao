package com.example.aaron.tiaotiao.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aaron.tiaotiao.R;

/**
 * Created by Aaron on 5/3/15.
 */
public class GuideFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.guide_layout, container, false);

        return rootView;
    }
}
