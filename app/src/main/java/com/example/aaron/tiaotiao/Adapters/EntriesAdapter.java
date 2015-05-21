package com.example.aaron.tiaotiao.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aaron.tiaotiao.R;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by Aaron on 5/3/15.
 */
public class EntriesAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private LinkedList<HashMap<String, Object>> items;
    private Context mContext;

    static final String KEY_IMG = "img";        //旅店略缩图
    static final String KEY_ID = "id";          //旅店名字
    static final String KEY_BRIEF = "brief";    //旅店摘要（如简介、位置信息等）
    static final String KEY_PERIOD = "period";  //打工时间
    static final String KEY_PRICE = "price";    //旅店价格

    public EntriesAdapter(Context context, LinkedList<HashMap<String, Object>> items) {
        mInflater = LayoutInflater.from(context);
        this.items = items;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        final Bitmap bitmap = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.recommendentries, null);

            holder = new ViewHolder();
            holder.holder_room_img = (ImageView) convertView.findViewById(R.id.room_img);
            holder.holder_room_id = (TextView) convertView.findViewById(R.id.room_id);
            holder.holder_room_brief = (TextView) convertView.findViewById(R.id.room_brief);
            holder.holder_room_period = (TextView) convertView.findViewById(R.id.room_period);
            holder.holder_room_price = (TextView) convertView.findViewById(R.id.room_price);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        setImageView(holder, position);
        holder.holder_room_id.setText(items.get(position).get(KEY_ID).toString());
        holder.holder_room_brief.setText(items.get(position).get(KEY_BRIEF).toString());
        holder.holder_room_period.setText(items.get(position).get(KEY_PERIOD).toString());
        holder.holder_room_price.setText(items.get(position).get(KEY_PRICE).toString());

        return convertView;
    }

    private void setImageView(final ViewHolder holder, final int position) {
        final Bitmap[] bitmap = {null};
        new AsyncTask() {

            @Override
            protected Object doInBackground(Object[] params) {
                try {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) (new URL(items.get(position).get(KEY_IMG).toString())).openConnection();
                    bitmap[0] = BitmapFactory.decodeStream(httpURLConnection.getInputStream());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                holder.holder_room_img.setImageBitmap(bitmap[0]);
            }
        }.execute();
    }


    private class ViewHolder {
        ImageView holder_room_img;
        TextView holder_room_id;
        TextView holder_room_brief;
        TextView holder_room_period;
        TextView holder_room_price;
    }
}
