package com.example.nebul.cnaapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class LazyNewsAdapter extends BaseAdapter {
    
    private Activity activity;
    private ArrayList<News> listData;
    private LayoutInflater inflater = null;

    public LazyNewsAdapter(Activity activity, ArrayList<News> listData) {
        this.activity = activity;
        this.listData = listData;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return listData.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        News newsItem = listData.get(position);

        View view = convertView;
        if(convertView == null)
            view = inflater.inflate(R.layout.news_cell, null);

        TextView newsTitle = (TextView) view.findViewById(R.id.newsTitle);
        TextView newsDate = (TextView) view.findViewById(R.id.newsDate);
        ImageView image = (ImageView) view.findViewById(R.id.newsImage);

        newsTitle.setText(newsItem.getNewsTitle());
        newsDate.setText(newsItem.getNewsDate());
        String url = newsItem.getNewsUrl();

        ImageLoader imageLoader = new ImageLoader(activity, 600, R.drawable.info);
        imageLoader.displayImage(url, image);

        return view;
    }
}