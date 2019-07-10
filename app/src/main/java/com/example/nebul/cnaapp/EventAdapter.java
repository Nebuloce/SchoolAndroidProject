package com.example.nebul.cnaapp;

import android.widget.ArrayAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class EventAdapter extends ArrayAdapter <String> {
    private Context context;
    private String[] items;

    public EventAdapter(Context context, String[] items) {
        super(context, R.layout.eventrowlist, R.id.textid, items);
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.eventrowlist, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.textid);
        textView.setText(items[position]);
        return rowView;
    }
}