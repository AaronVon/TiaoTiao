package com.example.aaron.tiaotiao.Activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aaron.tiaotiao.Parsers.XMLParser;
import com.example.aaron.tiaotiao.R;
import com.example.aaron.tiaotiao.WebUtility.SetThumbnailIMG;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by Aaron on 5/30/15.
 */
public class GuideJump extends SwipeBackActivity{
    static final String KEY_JUMP = "jump";
    static final String NODE_NAME = "Skills";

    static final String KEY_TITLE = "title";
    static final String KEY_USERNAME = "username";
    static final String KEY_GENDER = "gender";
    static final String KEY_DATE = "date";
    static final String KEY_CONTENT = "content";
    static final String KEY_IMG = "img";
    String jump_url = "";

    ImageView guide_img;
    TextView guide_title, guide_username, guide_user_gender, guide_date, guide_content;

    @Override
    public void onBackPressed() {
        scrollToFinishActivity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide_jump);

        initView();
    }

    private void initView() {
        guide_img = (ImageView) findViewById(R.id.guidejump_img);
        guide_title = (TextView) findViewById(R.id.guidejump_title);
        guide_username = (TextView) findViewById(R.id.guidejump_username);
        guide_user_gender = (TextView) findViewById(R.id.guidejump_gender);
        guide_date = (TextView) findViewById(R.id.guidejump_date);
        guide_content = (TextView) findViewById(R.id.guidejump_content);

        Bundle bundle = getIntent().getExtras();
        jump_url = bundle.getString(KEY_JUMP).toString();
        try {
            jump_url = URLEncoder.encode(jump_url, "utf-8");
            jump_url = jump_url.replaceAll("%3A", ":");
            jump_url = jump_url.replaceAll("%2F", "/");

            Log.d("URL_2", jump_url);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        new AsyncTask() {
            NodeList nodeList;
            XMLParser xmlParser;
            Document document;
            Element element;

            @Override
            protected Object doInBackground(Object[] params) {
                xmlParser = new XMLParser();
                String xml = xmlParser.getXmlFromUrl(jump_url);
                document = xmlParser.getDomElement(xml);
                nodeList = document.getElementsByTagName(NODE_NAME);

                //there is only one node in nodelist, so it would be much easier
                element = (Element) nodeList.item(0);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                new SetThumbnailIMG().execute(guide_img, xmlParser.getValue(element, KEY_IMG).toString());
                guide_title.setText(xmlParser.getValue(element, KEY_TITLE).toString());
                guide_username.setText(xmlParser.getValue(element, KEY_USERNAME).toString());
                guide_user_gender.setText(xmlParser.getValue(element, KEY_GENDER).toString());
                guide_date.setText(xmlParser.getValue(element, KEY_DATE).toString());
                guide_content.setText(xmlParser.getValue(element, KEY_CONTENT).toString());
            }
        }.execute();

    }
}
