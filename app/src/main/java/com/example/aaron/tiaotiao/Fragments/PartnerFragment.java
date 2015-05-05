package com.example.aaron.tiaotiao.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aaron.tiaotiao.R;

/**
 * Created by Aaron on 5/2/15.
 */
public class PartnerFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.partner_layout, container, false);

        return rootView;
    }
}
