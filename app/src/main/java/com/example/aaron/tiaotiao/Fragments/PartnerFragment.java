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
import com.example.aaron.tiaotiao.Utilities.NetworkStatusUtil;
import com.example.aaron.tiaotiao.WebUtility.LoadXML;

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

    static final String partnerURL = "http://s.mymusise.com/pman/m";

    static final String KEY_PARTNER = "pman";   //node tag

    static final String KEY_ID = "id";
    static final String KEY_GENDER = "gender";
    static final String KEY_THUMBNAIL = "img";
    static final String KEY_BRIEF = "brief";
    static final String KEY_DESTINATION = "destination";
    static final String KEY_JUMP = "jump";      //二级请求

    static final String KEY_NETINFO = "netinfo";
    boolean netAvaiable;

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
        netAvaiable = new NetworkStatusUtil(mContext).isAvailable();
        mLinkedList = new LinkedList<>();
        NodeList nodeList;
        XMLParser xmlParser;
        Document document;
        String xml = new LoadXML().getXML(partnerURL, getResources().getString(R.string.PartnerFragment), netAvaiable);
        xmlParser = new XMLParser();
        document = xmlParser.getDomElement(xml);
        nodeList = document.getElementsByTagName(KEY_PARTNER);
        for (int i = 0; i < nodeList.getLength(); ++i) {
            HashMap<String, Object> hashMap = new HashMap<>();
            Element element = (Element) nodeList.item(i);
            hashMap.put(KEY_ID, xmlParser.getValue(element, KEY_ID));
            hashMap.put(KEY_GENDER, xmlParser.getValue(element, KEY_GENDER));
            hashMap.put(KEY_THUMBNAIL, xmlParser.getValue(element, KEY_THUMBNAIL));
            hashMap.put(KEY_BRIEF, xmlParser.getValue(element, KEY_BRIEF));
            hashMap.put(KEY_DESTINATION, xmlParser.getValue(element, KEY_DESTINATION));
            hashMap.put(KEY_JUMP, xmlParser.getValue(element, KEY_JUMP));

            mLinkedList.add(hashMap);
        }

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
                bundle.putBoolean(KEY_NETINFO, netAvaiable);
                intent.putExtras(bundle);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.keep);
            }
        });
    }


}
