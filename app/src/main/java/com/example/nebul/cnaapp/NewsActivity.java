package com.example.nebul.cnaapp;

import android.os.Build;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Menu;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.StringTokenizer;

@SuppressLint("NewApi")
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class NewsActivity extends Activity {

	private String type = null;
	private String date = null;
	private String description = null;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#055b8c")));
        
        Bundle extras = getIntent().getExtras();
        type = extras.getString("type");
        this.setTitle("Event");
        
        date = format(extras.getString("date"));
        description = extras.getString("description");
        
        setContentView(R.layout.news);               
        TextView tview = (TextView)findViewById(R.id.type);      
        TextView dview = (TextView) findViewById(R.id.date);
        TextView desview = (TextView) findViewById(R.id.description);
        
        tview.setText(type);
        dview.setText(date);
        
        description = description.replace("\\n", System.getProperty("line.separator"));
        desview.setSingleLine(false);
        desview.setText(description);
    }
    
    private String format(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");        
        try {
          date = sdf.parse(date).toString();
        }
        catch(Exception e) {}

        StringTokenizer st = new StringTokenizer(date);
        StringBuffer sb = new StringBuffer();
        String temp = st.nextToken();
        if(temp.equals("Mon"))
          sb.append("Monday, ");
        else if(temp.equals("Tue"))
          sb.append("Tuesday, ");
        else if(temp.equals("Wed"))
          sb.append("Wednesday, ");
        else if(temp.equals("Thu"))
          sb.append("Thursday, ");
        else if(temp.equals("Fri"))
          sb.append("Friday, ");
        else if(temp.equals("Sat"))
          sb.append("Saturday, ");
        else if(temp.equals("Sun"))
          sb.append("Sunday, ");

        
        temp = st.nextToken();
        if(temp.equals("Jan"))
          sb.append("January ");
        else if(temp.equals("Feb"))
          sb.append("February ");
        else if(temp.equals("Mar"))
          sb.append("March ");
        else if(temp.equals("Apr"))
          sb.append("April ");
        else if(temp.equals("May"))
          sb.append("May ");
        else if(temp.equals("Jun"))
          sb.append("June ");
        else if(temp.equals("Jul"))
          sb.append("July ");
        else if(temp.equals("Aug"))
          sb.append("August ");
        else if(temp.equals("Sep"))
          sb.append("September ");
        else if(temp.equals("Oct"))
          sb.append("October ");
        else if(temp.equals("Nov"))
          sb.append("November ");
        else if(temp.equals("Dec"))
          sb.append("December ");

        temp = st.nextToken();
        sb.append(temp + ", ");
        temp = st.nextToken();
        temp = st.nextToken();
        temp = st.nextToken();
        sb.append(temp);
        
        return sb.toString();
    }
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit, menu);
	//to-do Make Me Work
		return true;
	}
}