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

import com.example.aaron.tiaotiao.Activities.RecommendJump;
import com.example.aaron.tiaotiao.Adapters.EntriesAdapter;
import com.example.aaron.tiaotiao.MainActivity;
import com.example.aaron.tiaotiao.Parsers.XMLParser;
import com.example.aaron.tiaotiao.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by Aaron on 5/3/15.
 */
public class RecommendFragment extends Fragment {

    private LinkedList<HashMap<String, Object>> mLinkedList;
    private EntriesAdapter mAdapter;
    private Context mContext;
    private PullToRefreshListView mPullToRefreshListView;

    static final String recommendURL = "http://s.mymusise.com/display/m";

    static final String KEY_HOSTEL = "hostel";  //每一项的节点标记tag
    static final String KEY_IMG = "img";        //旅店略缩图
    static final String KEY_ID = "id";          //旅店名字
    static final String KEY_BRIEF = "brief";    //旅店摘要（如简介、位置信息等）
    static final String KEY_PERIOD = "period";  //打工时间
    static final String KEY_PRICE = "price";    //旅店价格
    static final String KEY_JUMP = "jump";      //二级请求

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recommend_layout, container, false);
        mPullToRefreshListView = (PullToRefreshListView) rootView.findViewById(R.id.pull_refresh_list);
        mContext = getActivity();
        initRecommendEntries();

        return rootView;
    }

    private void initRecommendEntries() {

        mLinkedList = new LinkedList<>();

        new AsyncTask() {
            NodeList nodeList;
            XMLParser xmlParser = null;
            Document doc = null;

            @Override
            protected Object doInBackground(Object[] params) {
                xmlParser = new XMLParser();
                String xml = xmlParser.getXmlFromUrl(recommendURL);
//                Log.d("---XML------XML---", xml);
                //get XML from recomendURL

                doc = xmlParser.getDomElement(xml);
                //get DOM elements
                nodeList = doc.getElementsByTagName(KEY_HOSTEL);
                //looping through all hostel nodes <hostel>
                for (int i = 0; i < nodeList.getLength(); ++i) {
                    HashMap<String, Object> tmp = new HashMap<>();
                    Element e = (Element) nodeList.item(i);
                    tmp.put(KEY_ID, xmlParser.getValue(e, KEY_ID));
                    tmp.put(KEY_IMG, xmlParser.getValue(e, KEY_IMG));
                    tmp.put(KEY_BRIEF, xmlParser.getValue(e, KEY_BRIEF));
                    tmp.put(KEY_PERIOD, xmlParser.getValue(e, KEY_PERIOD));
                    tmp.put(KEY_PRICE, xmlParser.getValue(e, KEY_PRICE));
                    tmp.put(KEY_JUMP, xmlParser.getValue(e, KEY_JUMP));

                    mLinkedList.add(tmp);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                mAdapter = new EntriesAdapter(mContext, mLinkedList);
                mPullToRefreshListView.setAdapter(mAdapter);
            }
        }.execute();


        mPullToRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView listView = (ListView) parent;
                position -= listView.getHeaderViewsCount();

                HashMap<String, Object> hashMap = (HashMap<String, Object>) mLinkedList.get(position);
                Intent intent = new Intent(mContext, RecommendJump.class);
                Bundle bundle = new Bundle();
                bundle.putString(KEY_JUMP, hashMap.get(KEY_JUMP).toString());
                intent.putExtras(bundle);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.keep);
                //RecommendJump activity sliding in from right
            }
        });

        mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
//                new GetDataTask().execute("top");
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
                new GetDataTask().execute("bottom");
            }
        });
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
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            switch (arg) {
                /*case "top":
//                    mLinkedList.addFirst("AAAAAAAAAA");
                    HashMap<String, Object> tmp = new HashMap<>();
                    tmp.put("img", R.drawable.hostel1);
                    tmp.put("name", "test");
                    mLinkedList.addFirst(tmp);
                    break;*/
                case "bottom":
                    Toast.makeText(mContext, "到底了哟", Toast.LENGTH_SHORT).show();
                    break;
            }
            mAdapter.notifyDataSetChanged();

            mPullToRefreshListView.onRefreshComplete();
            super.onPostExecute(o);
        }
    }
}
