package com.example.aaron.tiaotiao.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import com.example.aaron.tiaotiao.Adapters.GuideListAdapter;
import com.example.aaron.tiaotiao.Parsers.XMLParser;
import com.example.aaron.tiaotiao.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by Aaron on 5/3/15.
 */
public class GuideFragment extends Fragment {

    private LinkedList<HashMap<String, Object>> mLinkedList;
    private GuideListAdapter mAdapter;
    private PullToRefreshListView mPullToRefreshListView;
    private Context mContext;

    static final String guideURL = "http://s.mymusise.com/skills/m";
    static final String KEY_GUIDE = "skills";
    static final String KEY_IMG = "img";
    static final String KEY_ID = "id";
    static final String KEY_TITLE = "title";
    static final String KEY_BRIEF = "brief";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.guide_layout, container, false);
        mPullToRefreshListView = (PullToRefreshListView) rootView.findViewById(R.id.guide_pull_refresh_list);
        mContext = getActivity();

        initGuideList();
        return rootView;
    }

    private void initGuideList() {
        mLinkedList = new LinkedList<>();

        new AsyncTask() {
            NodeList nodeList;
            XMLParser xmlParser = null;
            Document document = null;

            @Override
            protected Object doInBackground(Object[] params) {
                xmlParser = new XMLParser();
                String xml = xmlParser.getXmlFromUrl(guideURL);
                document = xmlParser.getDomElement(xml);
                nodeList = document.getElementsByTagName(KEY_GUIDE);

                for (int i = 0; i < nodeList.getLength(); ++i) {
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    Element e = (Element) nodeList.item(i);

                    map.put(KEY_TITLE, xmlParser.getValue(e, KEY_TITLE));
                    map.put(KEY_IMG, xmlParser.getValue(e, KEY_IMG));
                    map.put(KEY_BRIEF, xmlParser.getValue(e, KEY_BRIEF));
                    map.put(KEY_ID, xmlParser.getValue(e, KEY_ID));

                    mLinkedList.add(map);
                }

                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                mAdapter = new GuideListAdapter(mContext, mLinkedList);
                mPullToRefreshListView.setAdapter(mAdapter);
                mPullToRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                });

                mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
                    @Override
                    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

                    }

                    @Override
                    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                        new GetDataTask().execute("bottom");
                    }
                });
            }
        }.execute();
    }

    private class GetDataTask extends AsyncTask {
        String arg;
        @Override
        protected Object doInBackground(Object[] params) {
            try {
                arg = params[0].toString();
                Thread.sleep(1000);
            } catch (Exception e) {
                Log.e("Error", e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            switch (arg) {
                case "top":
                    break;
                case "bottom":
                    break;
            }
            mAdapter.notifyDataSetChanged();
            mPullToRefreshListView.onRefreshComplete();
        }
    }
}
