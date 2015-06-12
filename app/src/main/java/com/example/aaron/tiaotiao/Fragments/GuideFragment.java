package com.example.aaron.tiaotiao.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.example.aaron.tiaotiao.Activities.GuideJump;
import com.example.aaron.tiaotiao.Adapters.GuideListAdapter;
import com.example.aaron.tiaotiao.Parsers.XMLParser;
import com.example.aaron.tiaotiao.R;
import com.example.aaron.tiaotiao.Utilities.NetworkStatusUtil;
import com.example.aaron.tiaotiao.WebUtility.LoadXML;
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
    static final String KEY_JUMP = "jump";      //二级请求

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
        NodeList nodeList;
        XMLParser xmlParser;
        Document document;
        String xml = new LoadXML().getXML(guideURL, getResources().getString(R.string.GuideFragment), new NetworkStatusUtil(mContext).isAvailable());

        xmlParser = new XMLParser();
        document = xmlParser.getDomElement(xml);
        nodeList = document.getElementsByTagName(KEY_GUIDE);
        for (int i = 0; i < nodeList.getLength(); ++i) {
            HashMap<String, Object> hashMap = new HashMap<>();
            Element element = (Element) nodeList.item(i);
            hashMap.put(KEY_TITLE, xmlParser.getValue(element, KEY_TITLE));
            hashMap.put(KEY_IMG, xmlParser.getValue(element, KEY_IMG));
            hashMap.put(KEY_BRIEF, xmlParser.getValue(element, KEY_BRIEF));
            hashMap.put(KEY_ID, xmlParser.getValue(element, KEY_ID));
            hashMap.put(KEY_JUMP, xmlParser.getValue(element, KEY_JUMP));
            mLinkedList.add(hashMap);
        }

        mAdapter = new GuideListAdapter(mContext, mLinkedList);
        mPullToRefreshListView.setAdapter(mAdapter);

        mPullToRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView listView = (ListView) parent;
                position -= listView.getHeaderViewsCount();

                HashMap<String, Object> hashMap = (HashMap<String, Object>) mLinkedList.get(position);
                Intent intent = new Intent(mContext, GuideJump.class);
                Bundle bundle = new Bundle();
                bundle.putString(KEY_JUMP, hashMap.get(KEY_JUMP).toString());
                intent.putExtras(bundle);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.keep);
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
                    Toast.makeText(mContext, "到底了哟", Toast.LENGTH_SHORT).show();
                    break;
            }
            mAdapter.notifyDataSetChanged();
            mPullToRefreshListView.onRefreshComplete();
        }
    }
}
