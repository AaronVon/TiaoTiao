package com.example.aaron.tiaotiao.Fragments;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.aaron.tiaotiao.Activities.PartnerJump;
import com.example.aaron.tiaotiao.Adapters.PartnerListAdapter;
import com.example.aaron.tiaotiao.Parsers.XMLParser;
import com.example.aaron.tiaotiao.R;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by Aaron on 5/2/15.
 */
public class PartnerFragment extends Fragment{

    private LinkedList<HashMap<String, Object>> mLinkedList;
    private PartnerListAdapter mAdapter;
    private ListView mListView;
    private Context mContext;
    private boolean isNetworkAvailable;

    static final String partnerURL = "http://s.mymusise.com/pman/m";

    static final String KEY_PARTNER = "pman";   //node tag

    static final String KEY_ID = "id";
    static final String KEY_GENDER = "gender";
    static final String KEY_THUMBNAIL = "img";
    static final String KEY_BRIEF = "brief";
    static final String KEY_DESTINATION = "destination";
    static final String KEY_JUMP = "jump";      //二级请求

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.partner_layout, container, false);
        mListView = (ListView) rootView.findViewById(R.id.activity_googlecards_listview);
        mContext = getActivity();
        initPartnerList();
        
        return rootView;
    }

    private void initPartnerList() {
        mLinkedList = new LinkedList<>();
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getActiveNetworkInfo() != null) {
            isNetworkAvailable = connectivityManager.getActiveNetworkInfo().isAvailable();
        }

        new AsyncTask() {
            NodeList nodeList;
            XMLParser xmlParser = null;
            Document document = null;

            @Override
            protected Object doInBackground(Object[] params) {
                xmlParser = new XMLParser();
                String xml = xmlParser.getXmlFromUrl(partnerURL);

                document = xmlParser.getDomElement(xml);
                nodeList = document.getElementsByTagName(KEY_PARTNER);

                for (int i = 0; i < nodeList.getLength() - 5; ++i) {
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    Element e = (Element) nodeList.item(i);
                    map.put(KEY_ID, xmlParser.getValue(e, KEY_ID));
                    map.put(KEY_THUMBNAIL, xmlParser.getValue(e, KEY_THUMBNAIL));
                    map.put(KEY_GENDER, xmlParser.getValue(e, KEY_GENDER));
                    map.put(KEY_DESTINATION, xmlParser.getValue(e, KEY_DESTINATION));
                    map.put(KEY_BRIEF, xmlParser.getValue(e, KEY_BRIEF));
                    map.put(KEY_JUMP, xmlParser.getValue(e, KEY_JUMP));
                    mLinkedList.add(map);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                mAdapter = new PartnerListAdapter(mContext, mLinkedList);
                mListView.setAdapter(mAdapter);

                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ListView listView = (ListView) parent;
                        position -= listView.getHeaderViewsCount();

                        HashMap<String, Object> hashMap = (HashMap<String, Object>) mLinkedList.get(position);
                        Intent intent = new Intent(mContext, PartnerJump.class);
                        Bundle bundle = new Bundle();
                        bundle.putString(KEY_JUMP, hashMap.get(KEY_JUMP).toString());
                        intent.putExtras(bundle);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.keep);
                    }
                });
            }
        }.execute();
    }


}
