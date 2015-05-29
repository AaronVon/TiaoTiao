package com.example.aaron.tiaotiao.Activities;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aaron.tiaotiao.MainActivity;
import com.example.aaron.tiaotiao.R;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.logging.LogRecord;


/**
 * Created by Aaron on 5/27/15.
 */
public class AccountActivity extends ActionBarActivity {

    ImageButton QQ_Login_button;
    ImageButton Weibo_Login_button;
    Button Register_Button;

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_layout);
        QQ_Login_button = (ImageButton) findViewById(R.id.QQ_Login);
        Weibo_Login_button = (ImageButton) findViewById(R.id.weibo_Login);
        Register_Button = (Button) findViewById(R.id.register);
        QQ_Login_button.setOnClickListener(new ButtonClickListener());
        Weibo_Login_button.setOnClickListener(new ButtonClickListener());
        Register_Button.setOnClickListener(new ButtonClickListener());

        textView = (TextView) findViewById(R.id.test_text);
//        doLogin();
    }


    private class ButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.QQ_Login:
//                    Toast.makeText(AccountActivity.this, "QQ login", Toast.LENGTH_SHORT).show();
                    doLoginQQ();
                    break;
                case R.id.weibo_Login:
                    Toast.makeText(getApplicationContext(), "Weibo login", Toast.LENGTH_SHORT).show();
                    doLoginWeibo();
                    break;

                case R.id.register:
                    Toast.makeText(getApplicationContext(), "register", Toast.LENGTH_SHORT).show();
                    doLoginRegister();
                    break;
            }
        }
    }

    private void doLoginQQ() {

        String mAppid = "1104591741";
        final Tencent mTencent = Tencent.createInstance(mAppid, getApplicationContext());

        mTencent.login(AccountActivity.this, "all", new IUiListener() {
            @Override
            public void onComplete(Object o) {
                Toast.makeText(getApplicationContext(), "Login successfully", Toast.LENGTH_SHORT).show();


                final Message message = new Message();
                message.obj = o;
                message.what = 0;
                mHandler.sendMessage(message);

                QQToken qqToken = mTencent.getQQToken();
                UserInfo info = new UserInfo(getApplicationContext(), qqToken);
                info.getUserInfo(new IUiListener() {
                    @Override
                    public void onComplete(Object o) {
                        Message msg = new Message();
                        msg.obj = o;
                        msg.what = 1;
                        mHandler.sendMessage(msg);
                    }

                    @Override
                    public void onError(UiError uiError) {

                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }

            @Override
            public void onError(UiError uiError) {

            }

            @Override
            public void onCancel() {

            }
        });


    }

    private void doLoginWeibo() {
    }

    private void doLoginRegister() {

    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    String s = "";
                    try {
                        s = ((JSONObject) msg.obj).getString("openid");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    textView.append("openid : " + s);
                    break;
                case 1:
                    StringBuilder string = new StringBuilder();

                    JSONObject jsonObject = (JSONObject) msg.obj;
                    try {
                        string.append("huangzhuan: " + jsonObject.getString("is_yellow_year_vip") + "\n");
                        string.append("thumbnail_1: " + jsonObject.getString("figureurl_qq_1") + "\n");
                        string.append("thumbnail_2: " + jsonObject.getString("figureurl_qq_2") + "\n");
                        string.append("nickname: " + jsonObject.getString("nickname") + "\n");
                        string.append("city: " + jsonObject.getString("city") + "\n");
                        string.append("province: " + jsonObject.getString("province") + "\n");
                        string.append("gender: " + jsonObject.getString("gender") + "\n");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    textView.append("\n" + string.toString());
                    break;
            }
        }
    };
}
