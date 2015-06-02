package com.example.aaron.tiaotiao.OpenAPI;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.sso.SsoHandler;

/**
 * Created by Aaron on 6/2/15.
 */
public class Weibo_OpenAPI extends Activity {

    private AuthInfo mAuthInfo;
    private Oauth2AccessToken mAccessToken;
    private SsoHandler mSsoHandler;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        doLogin();
    }

    private void init() {
        mContext = getApplicationContext();
        mAuthInfo = new AuthInfo(mContext, Constants.Weibo_APP_KEY, Constants.Weibo_REDIRECT_URL, Constants.Weibo_SCOPE);
        mSsoHandler = new SsoHandler(this, mAuthInfo);
    }
    private void doLogin() {

    }

}
