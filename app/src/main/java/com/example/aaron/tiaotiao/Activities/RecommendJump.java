package com.example.aaron.tiaotiao.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aaron.tiaotiao.Parsers.XMLParser;
import com.example.aaron.tiaotiao.R;
import com.example.aaron.tiaotiao.WebImage.SetThumbnailIMG;

import org.apache.http.protocol.HTTP;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.BreakIterator;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by Aaron on 5/29/15.
 */
public class RecommendJump extends SwipeBackActivity {

    static final String KEY_JUMP = "jump";
    static final String NODE_NAME = "Display";

    static final String KEY_IMG = "img";
    static final String KEY_TITLE = "title";
    static final String KEY_PRICE = "price_avg";
    static final String KEY_HOSTELNAME = "hotelname";
    static final String KEY_USERNAME = "username";
    static final String KEY_DATE = "date";
    static final String KEY_PERIOD = "period";
    static final String KEY_CONTENT = "content";
    String jump_url = "";

    Button recommendjump_follow, recommendjump_message;

    private Context mContext;

    //swipeback
    private int[] mBgColors;

    private static int mBgIndex = 0;

    private String mKeyTrackingMode;

    private RadioGroup mTrackingModeGroup;

    private SwipeBackLayout mSwipeBackLayout;


    //layout
    private class ViewHolder {
        private ImageView hostel_img;
        private TextView hostel_title, price_avg, hostel_name, user_name, date, period, content;
    }

    @Override
    public void onBackPressed() {
        scrollToFinishActivity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recommend_jump);
        mContext = getApplicationContext();

        mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setScrimColor(Color.TRANSPARENT);
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);

        initView();

    }

    private void initView() {
        Bundle bundle = getIntent().getExtras();
        jump_url = bundle.getString(KEY_JUMP);
        Log.d("URL_1", jump_url);

        //解析中文 url
        try {
            jump_url = URLEncoder.encode(jump_url, "utf-8");
            jump_url = jump_url.replaceAll("%3A", ":");
            jump_url = jump_url.replaceAll("%2F", "/");

            Log.d("URL_2", jump_url);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        final ViewHolder holder = new ViewHolder();
        holder.hostel_img = (ImageView) findViewById(R.id.hostel_img);
        holder.hostel_title = (TextView) findViewById(R.id.hostel_title);
        holder.price_avg = (TextView) findViewById(R.id.price_avg);
        holder.hostel_name = (TextView) findViewById(R.id.hostel_name);
        holder.user_name = (TextView) findViewById(R.id.user_name);
        holder.date = (TextView) findViewById(R.id.date);
        holder.period = (TextView) findViewById(R.id.period);
        holder.content = (TextView) findViewById(R.id.content);

        //button
        recommendjump_follow = (Button) findViewById(R.id.recommendjump_follow);
        recommendjump_message = (Button) findViewById(R.id.recommendjump_message);
        recommendjump_follow.setOnClickListener(new MyButtonClickListener());
        recommendjump_message.setOnClickListener(new MyButtonClickListener());

        new AsyncTask() {
            NodeList nodeList;
            XMLParser xmlParser;
            Document document;
            Element element;

            @Override
            protected Object doInBackground(Object[] params) {
                xmlParser = new XMLParser();

                String xml = xmlParser.getXmlFromUrl(jump_url);
                //xml format error
                document = xmlParser.getDomElement(xml);
                nodeList = document.getElementsByTagName(NODE_NAME);

                //there is only one node in nodelist, so it would be much easier
                element = (Element) nodeList.item(0);

                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                new SetThumbnailIMG().execute(holder.hostel_img, xmlParser.getValue(element, KEY_IMG).toString());
                holder.hostel_title.setText(xmlParser.getValue(element, KEY_TITLE).toString());
                holder.price_avg.setText("￥"+xmlParser.getValue(element, KEY_PRICE).toString());
                holder.hostel_name.setText(xmlParser.getValue(element, KEY_HOSTELNAME).toString());
                holder.user_name.setText(xmlParser.getValue(element, KEY_USERNAME).toString());
                holder.date.setText(xmlParser.getValue(element, KEY_DATE).toString());
                holder.period.setText(xmlParser.getValue(element, KEY_PERIOD).toString());
                holder.content.setText(xmlParser.getValue(element, KEY_CONTENT).toString());

            }
        }.execute();
    }

    private class MyButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.recommendjump_follow:
                    Toast.makeText(mContext, "已关注", Toast.LENGTH_SHORT).show();

                    break;
                case R.id.recommendjump_message:
                    new AlertDialog.Builder(RecommendJump.this)
                            .setTitle("打个招呼")
                            .setView(new EditText(RecommendJump.this))
                            .setPositiveButton("发送", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(RecommendJump.this, "已发送", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("取消", null)
                            .show();
                    break;
            }
        }
    }
}
