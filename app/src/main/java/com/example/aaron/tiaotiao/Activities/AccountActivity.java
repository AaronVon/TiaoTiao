package com.example.aaron.tiaotiao.Activities;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aaron.tiaotiao.MainActivity;
import com.example.aaron.tiaotiao.R;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQAuth;
import com.tencent.connect.auth.QQToken;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.example.aaron.tiaotiao.WebImage.toRoundImg;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Aaron on 5/27/15.
 */
public class AccountActivity extends ActionBarActivity {

    ImageView mImageView;
    TextView mTextView;
    TextView mNickName;

    private Tencent mTencent;
    public static QQAuth mQQAuth;
    public static String mAppID = "1104591741";
    public static String openIDString;
    public static String nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_layout);

        mImageView = (ImageView) findViewById(R.id.image_thumbnail);
        mTextView = (TextView) findViewById(R.id.user_openID);
        mNickName = (TextView) findViewById(R.id.user_nickname);

        doLogin();
    }

    private void doLogin() {
        mTencent = Tencent.createInstance(mAppID, getApplicationContext());
        mTencent.login(AccountActivity.this, "all", new IUiListener() {
            @Override
            public void onComplete(Object o) {
                Toast.makeText(AccountActivity.this, "Login successfully", Toast.LENGTH_SHORT).show();
                try {
                    Log.d("DEBUG", o.toString());
                    openIDString = ((JSONObject) o).getString("openid");
                    nickname = ((JSONObject) o).getString("nickname");
                    mTextView.setText(openIDString);
                    mNickName.setText(nickname);

                } catch (Exception e) {
                    Log.e("ERORR", e.toString());
                }
            }

            @Override
            public void onError(UiError uiError) {

            }

            @Override
            public void onCancel() {

            }
        });

        QQToken qqToken = mTencent.getQQToken();
        UserInfo userInfo = new UserInfo(getApplicationContext(), qqToken);
        userInfo.getUserInfo(new IUiListener() {
            @Override
            public void onComplete(Object o) {
                JSONObject jsonObject = (JSONObject) o;
                try {
                    Log.d("user\nuser\nuser\n", jsonObject.toString());

                    String imgURL = jsonObject.getString("figureurl_qq_2");
                    toRoundImg roundImg = new toRoundImg(imgURL);
                    Bitmap bitmap = (Bitmap) roundImg.execute().get();
                    mImageView.setImageBitmap(bitmap);
                } catch (JSONException e) {
                    Log.e("JSON", e.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(UiError uiError) {

            }

            @Override
            public void onCancel() {

            }
        });

    }
}
