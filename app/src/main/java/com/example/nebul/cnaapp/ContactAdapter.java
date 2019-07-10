package com.example.nebul.cnaapp;

import android.widget.ArrayAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactAdapter extends ArrayAdapter <String> {
    private Context context;
    private String[] items;

    public ContactAdapter(Context context, String[] items) {
        super(context, R.layout.iconrowlist, R.id.ictextid, items);
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.iconrowlist, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.ictextid);
        TextView subTextView = (TextView) rowView.findViewById(R.id.icsubtextid);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.list_image);
        textView.setText(items[position]);

        // Change icon and subtext based on name
        String s = items[position];

        if (s.equals("Call")) {
            imageView.setImageResource(R.drawable.ic_action_call);
            subTextView.setText("709.758.9000");
        } else if (s.equals("Write")) {
            imageView.setImageResource(R.drawable.ic_action_email);
            subTextView.setText("info@etc.cna.nl.ca");
        } else if(s.equals("Visit")) {
        		imageView.setImageResource(R.drawable.ic_web);
            subTextView.setText("the website");
        }
        	else if (s.equals("Find")) {
            imageView.setImageResource(R.drawable.ic_action_location_found);
            subTextView.setText("on the map");
        }
            
        return rowView;
    }
}