package com.example.nebul.cnaapp;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ScheduleActivity extends Activity {
	final static String[] programs = { "Select Program", "Architectural", 
		"Civil", "Geomatics", "Computing Systems", "Biomedical", 
		"Instrumentation", "Electrical (Power)",
		"Telecommunications", "Chemical Processing", "Industrial", "Mechanical", 
		"Manufacturing", "Petroleum" };

	final static String[] years = { "Select Year", "First", "Second", "Third" };
	
	String p = new String();
	String y = new String();
	String cid = new String();
	String loc = new String();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_activity);
        
		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#055b8c")));        
                        
        Spinner spinner_p = (Spinner) findViewById(R.id.spinner_p);         
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ScheduleActivity.this,
        		R.layout.spinnerrow, R.id.spinner_id, programs);
        spinner_p.setAdapter(arrayAdapter);
        
        spinner_p.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        	
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                switch(pos) {
                		case 1 : cid = "ae"; break;
                		case 2 : cid = "ce"; break;
                		case 3 : cid = "ge"; break;
                		case 4 : cid = "cs"; break;
                		case 5 : cid = "eb"; break;
                		case 6 : cid = "ei"; break;
                		case 7 : cid = "ep"; break;
                		case 8 : cid = "te"; break;
                		case 9 : cid = "cp"; break;
                		case 10: cid = "in"; break;
                		case 11: cid = "me"; break;
                		case 12: cid = "mm"; break;
                		case 13: cid = "pe"; break;
                		default: break;
                }    					
                p = programs[pos];
            }
            
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });               
        
        Spinner spinner_y = (Spinner) findViewById(R.id.spinner_y); 
        ArrayAdapter<String> y_arrayAdapter = new ArrayAdapter<String>(ScheduleActivity.this,
        		R.layout.spinnerrow, R.id.spinner_id, years);
        spinner_y.setAdapter(y_arrayAdapter);
        
        spinner_y.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {        	

            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                y = years[pos];
            }
            
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });     
        	
        final Button b = (Button) findViewById(R.id.schedule);
        	
		b.setOnTouchListener(new View.OnTouchListener() {
    		@Override
    		public boolean onTouch(View v, MotionEvent event) {
				if(v == b) {          
					if(event.getAction() == MotionEvent.ACTION_DOWN) {
						v.setAlpha(.3f);
					} 
					else {
						v.setAlpha(1f);
					}
    		       }
				return false;
    		}
    	});
        	
        b.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View arg0) {                            	
        		if(p.equals("Select Program") || y.equals("Select Year"))
                   	new AlertDialog.Builder(ScheduleActivity.this).
                   	setTitle("Error").setMessage("No Program/Year selected").
                   	setNeutralButton("Close", null).show();
        		else {               		
					Calendar cal = Calendar.getInstance();
					SimpleDateFormat monthFormat = new SimpleDateFormat("M");  
					String month_name = monthFormat.format(cal.getTime());
                			
					int month = Integer.parseInt(month_name);
					int selectedYear = 0;
                			
					if(y.equals("First"))
						selectedYear = 1;
					else if(y.equals("Second"))
						selectedYear = 2;
					else if(y.equals("Third"))
						selectedYear = 3;
                			
					int semester = 0;
					if(month >= 9 && month <= 12) {
						semester = 3 * selectedYear - 2;
					}
					else if(month >= 1 && month <= 4) {
						semester = 3 * selectedYear - 1;
					}
					else {
						semester = 3 * selectedYear;
					}
                    			
					StringBuffer sb = new StringBuffer();
					sb.append("http://your-url.appspot.com/etcapp/timetables/timetable_"); sb.append(cid); sb.append(semester); sb.append(".xml");
					loc = sb.toString();
                	if(cid.equals("cs")) {
                        setContentView(R.layout.page);
                        Intent newsSelect = new Intent(getApplicationContext(), TimeTableAdapter.class);
                        startActivity(newsSelect);
                       // Toast.makeText(ScheduleActivity.this, "Coming!", Toast.LENGTH_SHORT).show();
                    }else
                        Toast.makeText(ScheduleActivity.this, "You Selected : "
                                + loc, Toast.LENGTH_SHORT).show();
				}           			        			
			}
		});        
	}
}