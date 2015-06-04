package com.example.aaron.tiaotiao.OpenAPI;

import android.content.Context;
import android.content.SharedPreferences;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;

/**
 * Created by Aaron on 6/2/15.
 */
public class AccessTokenKeeper_Weibo {
    private static final String PREFERENCE_NAME = "Weibo_AccessToken_Preference";
    private static final String KEY_UID = "uid";
    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_EXPIRES_IN = "expires_in";

    /**
     * wirte Token object to SharedPreference named PREFERENCE_NAME
     * @param context
     * @param token Token object
     * */
    public static void writeAccessToken(Context context, Oauth2AccessToken token) {
        if (context == null || token == null) {
            return;
        } else {
            SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_APPEND);
            SharedPreferences.Editor editor = preferences.edit();
            //If a preferences file by this name does not exist,
            //it will be created when you retrieve an editor (SharedPreferences.edit())

            editor.putString(KEY_UID, token.getUid());
            editor.putString(KEY_ACCESS_TOKEN, token.getToken());
            editor.putLong(KEY_EXPIRES_IN, token.getExpiresTime());
            editor.commit();
        }
    }

    /**
     * get Token object from SharedPreference
     * @param context
     * @return return Token object
     * */

    public static Oauth2AccessToken readAccessToken(Context context) {
        if (context == null) {
            return null;
        } else {
            Oauth2AccessToken token = new Oauth2AccessToken();
            SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_APPEND);
            token.setUid(preferences.getString(KEY_UID, ""));
            token.setToken(preferences.getString(KEY_ACCESS_TOKEN, ""));
            token.setExpiresTime(preferences.getLong(KEY_EXPIRES_IN, 0));

            return token;
        }
    }

    /**
     * clear Token info form SharedPreference file
     * @param context
     * */
    public static void clear(Context context) {
        if (context == null) {
            return;
        } else {
            SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_APPEND);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.commit();
        }

    }

}
