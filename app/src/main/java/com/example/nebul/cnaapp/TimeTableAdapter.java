package com.example.nebul.cnaapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class TimeTableAdapter extends SimpleAdapter  {
    private Context context;
    private List <Map<String, String>> items;
    private int loc, today, hour;
    
    public TimeTableAdapter(Context context, List<Map<String, String>> items, int loc) {
        super(context, items, R.layout.timetablerowlists, new String[] { "time", "description" }, new int[] { R.id.time, R.id.description });
        this.context = context;
        this.items = items;     
        this.loc = loc;
        
        Calendar cal = Calendar.getInstance();
        today = cal.get(Calendar.DAY_OF_WEEK) - 2;
        hour = cal.get(Calendar.HOUR_OF_DAY);
        if(hour > 12 && hour < 19)
        		hour = hour - 12;
        
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.timetablerowlists, parent, false);
        
        TextView timeView = (TextView) rowView.findViewById(R.id.time);
        TextView classView = (TextView) rowView.findViewById(R.id.description);
        
        Map<String, String> map = (Map<String, String>) items.get(position);
                     
        if(loc == today) {
        		String h = map.get("time");
        		if(h.equals("noon"))
        			h = "12";
        		if(h.equals(Integer.toString(hour))) {
        			timeView.setTypeface(null, Typeface.BOLD);		
        			timeView.setTextColor(Color.RED);        			
        		}
        }
                       
        timeView.setText(map.get("time"));
        classView.setText(map.get("description"));
        
        if(!classView.getText().equals("")) {
        		classView.setBackgroundColor(Color.parseColor("#33b7ee"));
        		classView.setTextColor(Color.WHITE);
        }
        
        return rowView;
    }
}