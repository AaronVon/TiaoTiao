package com.example.aaron.tiaotiao.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aaron.tiaotiao.LRUImageLoader.ImageLoader;
import com.example.aaron.tiaotiao.R;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;

import com.example.aaron.tiaotiao.WebUtility.toRoundImg;

/**
 * Created by Aaron on 5/24/15.
 */
public class PartnerListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private LinkedList<HashMap<String, Object>> items;
    private ImageLoader mImageLoader;

    static final String KEY_IMG = "img";                //用户头像
    static final String KEY_ID = "id";                  //用户ID
    static final String KEY_GENDER = "gender";          //用户性别
    static final String KEY_DESTINATION = "destination";//用户目的地
    static final String KEY_BRIEF = "brief";

    public PartnerListAdapter(Context context, LinkedList<HashMap<String, Object>> items) {
        mInflater = LayoutInflater.from(context);
        this.items = items;
        mImageLoader = new ImageLoader();
    }



    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Integer getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.partnerlist, null);

            holder = new ViewHolder();
            holder.user_thumbnail = (ImageView) convertView.findViewById(R.id.user_thumbnail);
            holder.user_id = (TextView) convertView.findViewById(R.id.user_id);
            holder.user_gender = (TextView) convertView.findViewById(R.id.user_gender);
            holder.user_destination = (TextView) convertView.findViewById(R.id.user_destination);
            holder.user_brief = (TextView) convertView.findViewById(R.id.user_brief);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        /*new SetThumbnailIMG().execute(holder.user_thumbnail, items.get(position).get(KEY_IMG).toString());
        toRoundImg roundImg = new toRoundImg(items.get(position).get(KEY_IMG).toString());

        try {
            Bitmap bitmap = (Bitmap) roundImg.execute().get();
            holder.user_thumbnail.setImageBitmap(bitmap);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }*/

        mImageLoader.displayImage(items.get(position).get(KEY_IMG).toString(), holder.user_thumbnail);
        holder.user_id.setText(items.get(position).get(KEY_ID).toString());
        holder.user_gender.setText(items.get(position).get(KEY_GENDER).toString());
        holder.user_destination.setText(items.get(position).get(KEY_DESTINATION).toString());
        holder.user_brief.setText(items.get(position).get(KEY_BRIEF).toString());

        return convertView;
    }

    private class ViewHolder {
        ImageView user_thumbnail;
        TextView user_id;
        TextView user_gender;
        TextView user_destination;
        TextView user_brief;
    }
}
