package com.example.aaron.tiaotiao.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aaron.tiaotiao.Parsers.XMLParser;
import com.example.aaron.tiaotiao.R;
import com.example.aaron.tiaotiao.WebUtility.SetThumbnailIMG;
import com.example.aaron.tiaotiao.WebUtility.toRoundImg;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by Aaron on 5/30/15.
 */
public class PartnerJump extends SwipeBackActivity {
    static final String KEY_JUMP = "jump";
    static final String NODE_NAME = "Partner";

    static final String KEY_USERNAME = "username";
    static final String KEY_TITLE = "title";
    static final String KEY_GENDER = "gender";
    static final String KEY_DATE = "date";
    static final String KEY_DESTINATION = "destination";
    static final String KEY_IMG = "img";
    String jump_url = "";

    ImageView user_img;
    TextView username, title, gender, date, destination;

    Button follow_button, message_button;
    static final String KEY_NETINFO = "netinfo";
    boolean netAvailable;
    private Context mContext;

    @Override
    public void onBackPressed() {
        scrollToFinishActivity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        netAvailable = bundle.getBoolean(KEY_NETINFO, false);
        if (!netAvailable) {
            setContentView(R.layout.blank_layout);
        } else {
            setContentView(R.layout.partner_jump);
            mContext = getApplicationContext();
            initView();
        }

    }

    private void initView() {
        user_img = (ImageView) findViewById(R.id.partner_user_img);
        username = (TextView) findViewById(R.id.partner_username);
        title = (TextView) findViewById(R.id.partner_title);
        gender = (TextView) findViewById(R.id.partner_gender);
        destination = (TextView) findViewById(R.id.partner_destination);

        follow_button = (Button) findViewById(R.id.partnerjump_follow);
        message_button = (Button) findViewById(R.id.partnerjump_message);

        follow_button.setOnClickListener(new MyButtonClickListener());
        message_button.setOnClickListener(new MyButtonClickListener());
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
                new SetThumbnailIMG().execute(user_img, xmlParser.getValue(element, KEY_IMG).toString());
                toRoundImg roundImg = new toRoundImg(xmlParser.getValue(element, KEY_IMG).toString());
                Bitmap bitmap = null;
                try {
                    bitmap = (Bitmap) roundImg.execute().get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                user_img.setImageBitmap(bitmap);
                username.setText(xmlParser.getValue(element, KEY_USERNAME).toString());
                title.setText(xmlParser.getValue(element, KEY_TITLE).toString());
                gender.setText(xmlParser.getValue(element, KEY_GENDER).toString());
                destination.setText(xmlParser.getValue(element, KEY_DESTINATION).toString());

            }
        }.execute();
    }

    private class MyButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.partnerjump_follow:
                    Toast.makeText(mContext, "已关注", Toast.LENGTH_SHORT).show();

                    break;
                case R.id.partnerjump_message:
                    new AlertDialog.Builder(PartnerJump.this)
                            .setTitle("打个招呼")
                            .setView(new EditText(PartnerJump.this))
                            .setPositiveButton("发送", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(PartnerJump.this, "已发送", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("取消", null)
                            .show();

                    break;
            }
        }
    }
}
