package com.example.aaron.tiaotiao.Fragments;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.aaron.tiaotiao.EntriesAdapter;
import com.example.aaron.tiaotiao.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by Aaron on 5/3/15.
 */
public class RecommendFragment extends Fragment {

    private LinkedList<HashMap<String, Object>> mLinkedList;
    private EntriesAdapter mAdapter;
//    private ArrayAdapter<String> mAdapter;
    private PullToRefreshListView mPullToRefreshListView;

    private String[] mStrings = { "Abbaye de Belloc", "Abbaye du Mont des Cats" };
    private int[] icons = {R.drawable.hostel1, R.drawable.hostel2};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recommend_layout, container, false);

        mPullToRefreshListView = (PullToRefreshListView) rootView.findViewById(R.id.pull_refresh_list);

        mLinkedList = new LinkedList<>();
//        mLinkedList.addAll(Arrays.asList(mStrings));
        for (int i = 0; i < mStrings.length; ++i) {
            HashMap<String, Object> tmp = new HashMap<>();
            tmp.put("img", icons[i]);
            tmp.put("name", mStrings[i]);
            mLinkedList.add(tmp);
        }
        mAdapter = new EntriesAdapter(getActivity(), mLinkedList);
//        mAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mLinkedList);

        mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                new GetDataTask().execute("top");
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                new GetDataTask().execute("bottom");
            }
        });

//        mPullToRefreshListView.setAdapter(mAdapter);
        mPullToRefreshListView.setAdapter(mAdapter);
        return rootView;
    }

    private class GetDataTask extends AsyncTask{
        String arg;
        @Override
        protected Object doInBackground(Object[] params) {
            try {
                arg = params[0].toString();
                Thread.sleep(1000);
            } catch (Exception e) {

            }
            return mStrings;
        }

        @Override
        protected void onPostExecute(Object o) {
            switch (arg) {
                case "top":
//                    mLinkedList.addFirst("AAAAAAAAAA");
                    HashMap<String, Object> tmp = new HashMap<>();
                    tmp.put("img", R.drawable.hostel1);
                    tmp.put("name", "test");
                    mLinkedList.addFirst(tmp);
                    break;
                case "bottom":
//                    mLinkedList.addLast("ZZZZZZZZZZZ");
                    break;
            }
            mAdapter.notifyDataSetChanged();

            mPullToRefreshListView.onRefreshComplete();
            super.onPostExecute(o);
        }
    }
}
