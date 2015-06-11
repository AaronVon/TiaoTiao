package com.example.aaron.tiaotiao.Adapters;

import android.content.Context;
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

/**
 * Created by Aaron on 5/25/15.
 */
public class GuideListAdapter extends BaseAdapter {

    private LayoutInflater mLayoutInflater;
    private LinkedList<HashMap<String, Object>> items;
    private ImageLoader mImageLoader;

    static final String KEY_IMG = "img";
    static final String KEY_ID = "id";
    static final String KEY_TITLE = "title";
    static final String KEY_BRIEF = "brief";

    public GuideListAdapter(Context context, LinkedList<HashMap<String, Object>> items) {
        mLayoutInflater = LayoutInflater.from(context);
        this.items = items;
        mImageLoader = new ImageLoader();
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
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.guidelist, null);
            holder = new ViewHolder();

            holder.guide_img = (ImageView) convertView.findViewById(R.id.guide_img);
            holder.guide_title = (TextView) convertView.findViewById(R.id.guide_title);
            holder.guide_id = (TextView) convertView.findViewById(R.id.guide_id);
            holder.guide_brief = (TextView) convertView.findViewById(R.id.guide_brief);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

//        new SetThumbnailIMG().execute(holder.guide_img, items.get(position).get(KEY_IMG).toString());
        mImageLoader.displayImage(items.get(position).get(KEY_IMG).toString(), holder.guide_img);
        holder.guide_title.setText(items.get(position).get(KEY_TITLE).toString());
        holder.guide_id.setText(items.get(position).get(KEY_ID).toString());
        holder.guide_brief.setText(items.get(position).get(KEY_BRIEF).toString());

        return convertView;
    }

    private class ViewHolder {
        ImageView guide_img;
        TextView guide_title;
        TextView guide_id;
        TextView guide_brief;
    }
}
