package com.example.aaron.tiaotiao;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Aaron on 5/3/15.
 */
public class EntriesAdapter extends BaseAdapter {
    private String[] strings = null;
    private int[] icons = null;
    private LayoutInflater mInflater;
    private LinkedList<HashMap<String, Object>> items;

    public EntriesAdapter(Context context, LinkedList<HashMap<String, Object>> items) {
        mInflater = LayoutInflater.from(context);
        this.items = items;

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
            convertView = mInflater.inflate(R.layout.entries, null);

            holder = new ViewHolder();
            holder.holderImageView = (ImageView) convertView.findViewById(R.id.entriesImageView);
            holder.holderTextView = (TextView) convertView.findViewById(R.id.entriesTextView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.holderImageView.setAdjustViewBounds(true);
        holder.holderImageView.setImageResource(Integer.parseInt(String.valueOf(items.get(position).get("img").toString())));
        holder.holderTextView.setText(items.get(position).get("name").toString());

        return convertView;
    }

    private class ViewHolder {
        ImageView holderImageView;
        TextView holderTextView;
    }
}
