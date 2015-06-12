package com.example.aaron.tiaotiao.WebUtility;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.example.aaron.tiaotiao.R;
import com.example.aaron.tiaotiao.Utilities.ExternalStorageUtil;

import org.apache.http.util.EncodingUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Aaron on 6/11/15.
 */
public class LoadXML {
    private static final String TAG_D = "LoadXML";
    private ExternalStorageUtil storageUtil;

    public LoadXML() {
        storageUtil = new ExternalStorageUtil();
    }

    public String getXML(String xmlUrl, String fragment, boolean isNetworkAvailable) {
        if (isNetworkAvailable) {
            return loadFromHTTP(xmlUrl, fragment);
        } else {
            return loadFromSD(fragment);
        }
    }

    public String loadFromHTTP(String xmlUrl, String fragment) {
        String xml = null;
        String[] params = {xmlUrl, fragment};
        try {
            xml = new LoadAsync().execute(params).get().toString();
            if (null == xml || xml == "") {
                xml = loadFromSD(fragment);
            }
        } catch (Exception e) {
            Log.d(TAG_D, e.toString());
        }
        return xml;
    }

    public String loadFromSD(String fragment) {
        final String CACHE_DIR = "TiaoTiao/xmlCache";
        String sdPath = storageUtil.getExternalStoragePath();
        String cachePath = sdPath + "/" + CACHE_DIR;
        File dirFile = new File(cachePath);
        File xmlFile = new File(cachePath + "/" + fragment + ".xml");
        StringBuilder xml = new StringBuilder();

        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(xmlFile), "utf-8"));
            String tmp;
            while ((tmp = bufferedReader.readLine()) != null) {
                xml.append(tmp);
            }
        } catch (Exception e) {
            Log.d(TAG_D, e.toString());
        }
        return xml.toString();
    }

    private class LoadAsync extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {
            StringBuilder xml = new StringBuilder();
            try {
                HttpURLConnection httpURLConnection = (HttpURLConnection) (new URL(params[0].toString())).openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String tmp;
                while ((tmp = bufferedReader.readLine()) != null) {
                    xml.append(tmp);
                }

                //update xml file on SD card
                String ExternalStoragePath = new ExternalStorageUtil().getExternalStoragePath();

                String xmlFileName = ExternalStoragePath + params[1].toString() + ".xml";
                File xmlFile = new File(xmlFileName);
                if (!xmlFile.exists()) {
                    xmlFile.createNewFile();
                }
                FileOutputStream fileOutputStream = new FileOutputStream(xmlFile);
                byte[] bytes = xml.toString().getBytes();
                fileOutputStream.write(bytes);
                fileOutputStream.close();

            } catch (Exception e) {
                Log.d(TAG_D, e.toString());
            }
            Log.d(TAG_D, "hello this is xml content: " + xml.toString());
            /*//如果 http 获取列表失败，则从本地SD 加载。
            if (xml.toString() == "" || xml.toString() == null) {
                return loadFromSD(params[1].toString());
            }*/

            return xml.toString();
        }
    }

}
