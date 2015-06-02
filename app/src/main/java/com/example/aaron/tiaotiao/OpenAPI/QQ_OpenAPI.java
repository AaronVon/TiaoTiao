package com.example.aaron.tiaotiao.OpenAPI;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

/**
 * Created by Aaron on 6/2/15.
 */
public class QQ_OpenAPI extends Activity {

    private Tencent mTencent;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();

        doLogin();
    }

    private void doLogin() {
        mTencent = Tencent.createInstance(Constants.QQ_APP_ID, mContext);
        mTencent.login(this, "all", new IUiListener() {
            @Override
            public void onComplete(Object o) {
                Toast.makeText(mContext, "login successfully", Toast.LENGTH_SHORT).show();
                finish();
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
