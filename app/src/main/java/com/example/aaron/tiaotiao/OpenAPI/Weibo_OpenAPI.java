package com.example.aaron.tiaotiao.OpenAPI;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aaron.tiaotiao.R;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

import java.text.SimpleDateFormat;

/**
 * Created by Aaron on 6/2/15.
 */
public class Weibo_OpenAPI extends Activity {

    /** 显示认证后的信息，如 AccessToken */
    private TextView mTokenText;

    private AuthInfo mAuthInfo;

    /** 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能  */
    private Oauth2AccessToken mAccessToken;

    /** 注意：SsoHandler 仅当 SDK 支持 SSO 时有效 */
    private SsoHandler mSsoHandler;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weibo_layout);

        mTokenText = (TextView) findViewById(R.id.textView_weibo);

        // 创建微博实例
        //mWeiboAuth = new WeiboAuth(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
        // 快速授权时，请不要传入 SCOPE，否则可能会授权不成功
        mAuthInfo = new AuthInfo(this, Constants.Weibo_APP_KEY, Constants.Weibo_REDIRECT_URL, Constants.Weibo_SCOPE);
        mSsoHandler = new SsoHandler(Weibo_OpenAPI.this, mAuthInfo);
        // 从 SharedPreferences 中读取上次已保存好 AccessToken 等信息，
        // 第一次启动本应用，AccessToken 不可用
        mAccessToken = AccessTokenKeeper_Weibo.readAccessToken(this);
        if (mAccessToken.isSessionValid()) {
            updateTokenView(true);
        }
        findViewById(R.id.button_weibo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSsoHandler.authorize(new WeiboAuthListener() {
                    @Override
                    public void onComplete(Bundle bundle) {
                        // 从 Bundle 中解析 Token
                        mAccessToken = Oauth2AccessToken.parseAccessToken(bundle);
                        if (mAccessToken.isSessionValid()) {
                            // 显示 Token
                            updateTokenView(false);

                            // 保存 Token 到 SharedPreferences
                            AccessTokenKeeper_Weibo.writeAccessToken(Weibo_OpenAPI.this, mAccessToken);
                            Toast.makeText(Weibo_OpenAPI.this,
                                    R.string.weibosdk_demo_toast_auth_success, Toast.LENGTH_SHORT).show();
                        } else {
                            // 以下几种情况，您会收到 Code：
                            // 1. 当您未在平台上注册的应用程序的包名与签名时；
                            // 2. 当您注册的应用程序包名与签名不正确时；
                            // 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
                            String code = bundle.getString("code");
                            String message = getString(R.string.weibosdk_demo_toast_auth_failed);
                            if (!TextUtils.isEmpty(code)) {
                                message = message + "\nObtained the code: " + code;
                            }
                            Toast.makeText(Weibo_OpenAPI.this, message, Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onWeiboException(WeiboException e) {
                        Toast.makeText(Weibo_OpenAPI.this, "Auth exception: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }
        });
//        init();
    }

    /**
     * initiate activity status
     * */
    private void init() {
        mContext = getApplicationContext();
        mAuthInfo = new AuthInfo(mContext, Constants.Weibo_APP_KEY, Constants.Weibo_REDIRECT_URL, Constants.Weibo_SCOPE);
        mSsoHandler = new SsoHandler(this, mAuthInfo);
        mAccessToken = AccessTokenKeeper_Weibo.readAccessToken(mContext);

//        if Token exits, get it from preference file
//        else doLogin()
        if (mAccessToken.isSessionValid()) {
            updateTokenView(true);
        } else {
            doLogin();
        }
    }

    /**
     * get Token info
     * @param hasExisted check if Token object exists in preference file legally*/
    private void updateTokenView(boolean hasExisted) {
        String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(
                new java.util.Date(mAccessToken.getExpiresTime()));
        String format = getString(R.string.weibosdk_demo_token_to_string_format_1);
        mTokenText.setText(String.format(format, mAccessToken.getToken(), date));

        String message = String.format(format, mAccessToken.getToken(), date);
        if (hasExisted) {
            message = getString(R.string.weibosdk_demo_token_has_existed) + "\n" + message;
        }
        mTokenText.setText(message);
    }

    /**
     * when this activity finished,
     * this function will be called*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//      SSO 授权回调
//      重要：发起 SSO 登陆的 Activity 必须重写 onActivityResult
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    private void doLogin() {
        mSsoHandler.authorize(new WeiboAuthListener() {
            @Override
            public void onComplete(Bundle bundle) {
//                get Token from Bundle
                mAccessToken = Oauth2AccessToken.parseAccessToken(bundle);
                if (mAccessToken.isSessionValid()) {
                    updateTokenView(false);

                    AccessTokenKeeper_Weibo.writeAccessToken(mContext, mAccessToken);
                    Toast.makeText(mContext, "Login successfully", Toast.LENGTH_SHORT).show();
                } else {
//                    以下几种情况，您会收到 Code：
//                    1. 当您未在平台上注册的应用程序的包名与签名时；
//                    2. 当您注册的应用程序包名与签名不正确时；
//                    3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
                    String code = bundle.getString("code");
                    String message = getString(R.string.weibosdk_demo_toast_auth_failed);
                    if (!TextUtils.isEmpty(code)) {
                        message += "\nObtained the code: " + code;
                    }
                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onWeiboException(WeiboException e) {
                Toast.makeText(mContext, "Auth exception: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(mContext, "Auth canceled", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
