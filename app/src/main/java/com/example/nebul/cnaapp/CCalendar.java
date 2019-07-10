package com.example.nebul.cnaapp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("NewApi")
@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class CCalendar {
	
	ArrayList<String> nameOfEvent;
	ArrayList<String> startDates;
	ArrayList<String> descriptions;
		
	ArrayList<String> type;
 	ArrayList<String> date;
	ArrayList<String> description;

	public GregorianCalendar month, itemmonth;// calendar instances.

	public CalendarAdapter adapter;// adapter instance
	public Handler handler;// for grabbing some event values for showing the dot
							// marker.
	public ArrayList<String> items; // container to store calendar items which
									// needs showing the event marker

	LinearLayout rLayout;
	Activity parent;
	GridView gridview;
	ListView eventsListView;
	
	String initialDate;
	String init;
	int currentPosition, initPosition;	
	
	public CCalendar(Activity parent, final ArrayList<String> nameOfEvent,
			final ArrayList<String> startDates, final ArrayList<String> descriptions) {
		this.parent = parent;
		this.nameOfEvent = nameOfEvent;
		this.startDates = startDates;
		this.descriptions = descriptions;
		
		Locale.setDefault(Locale.US);
		
		rLayout = (LinearLayout) parent.findViewById(R.id.text);
		month = (GregorianCalendar) GregorianCalendar.getInstance();
		itemmonth = (GregorianCalendar) month.clone();		

		items = new ArrayList<String>();		
		adapter = new CalendarAdapter(parent, month);
			
		gridview = (GridView) parent.findViewById(R.id.gridview);
		gridview.setAdapter(adapter);
		
		init = android.text.format.DateFormat.format("yyyy-MM", month).toString();
		
		initialDate = adapter.curentDateString;				
		for(int i = 0; i < gridview.getCount(); i++) {
			if(initialDate.equals(gridview.getItemAtPosition(i))) {
				currentPosition = i;
				break;
			}				
		}
		initPosition = currentPosition;
				
		handler = new Handler();
		handler.post(calendarUpdater);
		
		TextView title = (TextView) parent.findViewById(R.id.title);
		title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));

		RelativeLayout previous = (RelativeLayout) parent.findViewById(R.id.previous);

		previous.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setPreviousMonth();
				String cur = android.text.format.DateFormat.format("yyyy-MM", month).toString();
				String prev = android.text.format.DateFormat.format("yyyy-MM-dd", month).toString();
								
				if(cur.equals(init)) {
					prev = initialDate;
					currentPosition = initPosition;
				} 
				
				adapter.curentDateString = prev;					
				refreshCalendar();
				
				if(!cur.equals(init))
					updateCurrentPosition(prev);
								
				if (((LinearLayout) rLayout).getChildCount() > 0) {
					((LinearLayout) rLayout).removeAllViews();
				}
				
				View view = gridview.getRootView();
				gridview.performItemClick(view, currentPosition, currentPosition);
			}
		});

		RelativeLayout next = (RelativeLayout) parent.findViewById(R.id.next);
		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setNextMonth();
				String next = android.text.format.DateFormat.format("yyyy-MM-dd", month).toString();	
				String cur = android.text.format.DateFormat.format("yyyy-MM", month).toString();
				
				if(cur.equals(init)) {					
					next = initialDate;
					currentPosition = initPosition;
				}
				
				adapter.curentDateString = next;					
				refreshCalendar();
				
				if(!cur.equals(init))
					updateCurrentPosition(next);
				
				if (((LinearLayout) rLayout).getChildCount() > 0) {
					((LinearLayout) rLayout).removeAllViews();
				}
				
				View view = gridview.getRootView();
				gridview.performItemClick(view, currentPosition, currentPosition);
			}
		});
						
		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				
				currentPosition = position;
				
				// removing the previous view if added
				if (((LinearLayout) rLayout).getChildCount() > 0) {
					((LinearLayout) rLayout).removeAllViews();					
				}
				
				type = new ArrayList<String>();
				date = new ArrayList<String>();
				description = new ArrayList<String>();
											
				String selectedGridDate = CalendarAdapter.dayString
						.get(currentPosition);								
				
				String[] separatedTime = selectedGridDate.split("-");
				String gridvalueString = separatedTime[2].replaceFirst("^0*",
						"");// taking last part of date. ie; 2 from 2012-12-02.
				int gridvalue = Integer.parseInt(gridvalueString);
				
				// navigate to next or previous month on clicking offdays.
				if ((gridvalue > 10) && (position < 8)) {					
					setPreviousMonth();																				 					
					adapter.curentDateString = selectedGridDate;
					refreshCalendar();
					updateCurrentPosition(selectedGridDate);
				} else if ((gridvalue < 7) && (position > 28)) {					
					setNextMonth();										
					adapter.curentDateString = selectedGridDate;
					refreshCalendar();
					updateCurrentPosition(selectedGridDate);
				} else { // inside the current month
					adapter.curentDateString = selectedGridDate;	
					
					// mystery ? does not reselect day like June 1
					
					if(currentPosition == 0) 
						refreshCalendar();					
				}
				
				((CalendarAdapter) parent.getAdapter()).setSelected(v);				
				
				/* multiple events on the same day */
				for (int i = 0; i < startDates.size(); i++) {
					if (startDates.get(i).equals(selectedGridDate)) {
						type.add(nameOfEvent.get(i));
						date.add(startDates.get(i));
						description.add(descriptions.get(i));
					}
				}

				if (type.size() > 0) {
					final String[] events = new String[type.size()];
					final String[] dates = new String[date.size()];
					final String[] descriptions = new String[description.size()];
					for (int i = 0; i < type.size(); i++) {
						events[i] = type.get(i);																		
						dates[i] = date.get(i);
						descriptions[i] = description.get(i);						
					}
										
					eventsListView = new ListView(parent.getContext());
					eventsListView.setAdapter(new EventAdapter(parent.getContext(), events));
			        eventsListView.setTextFilterEnabled(true);
			        rLayout.addView(eventsListView);
			        
			        eventsListView.setOnItemClickListener(new OnItemClickListener() {
			            @Override
			            public void onItemClick(AdapterView<?> parent, View view,
			            			int position, long id) {
			        			               			                
			                Intent newsScreen = new Intent(parent.getContext().getApplicationContext(), NewsActivity.class);			          
		                    newsScreen.putExtra("type", events[position]);
		                    newsScreen.putExtra("date", dates[position]);
		                    newsScreen.putExtra("description", descriptions[position]);		                    
		                    parent.getContext().startActivity(newsScreen);		                    
			            }
			        });          			        
				}
				type = null;
			}
		});
	}

	public void updateCurrentPosition(String date) {
		for(int i = 0; i < gridview.getCount(); i++) {
			if(date.equals(gridview.getItemAtPosition(i))) {
				currentPosition = i;
				break;
			}				
		}		
	}
	
	protected void setNextMonth() {		
		if (month.get(GregorianCalendar.MONTH) == month
				.getActualMaximum(GregorianCalendar.MONTH)) {
			month.set((month.get(GregorianCalendar.YEAR) + 1),
					month.getActualMinimum(GregorianCalendar.MONTH), 1);
		} else {
			month.set(GregorianCalendar.MONTH,
					month.get(GregorianCalendar.MONTH) + 1);			
		}
	}

	protected void setPreviousMonth() {	
		if (month.get(GregorianCalendar.MONTH) == month
				.getActualMinimum(GregorianCalendar.MONTH)) {
			month.set((month.get(GregorianCalendar.YEAR) - 1),
					month.getActualMaximum(GregorianCalendar.MONTH), 1);			
		} else {
			month.set(GregorianCalendar.MONTH,
					month.get(GregorianCalendar.MONTH) - 1);
		}
	}

	protected void showToast(String string) {
		Toast.makeText(parent, string, Toast.LENGTH_SHORT).show();
	}

	public void refreshCalendar() {		
		TextView title = (TextView) parent.findViewById(R.id.title);

		adapter.refreshDays();
		adapter.notifyDataSetChanged();
		handler.post(calendarUpdater); // generate some calendar items

		title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));
	}

	public Runnable calendarUpdater = new Runnable() {

		@Override
		public void run() {
			items.clear();

			// Print dates of the current week
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
			
			for (int i = 0; i < startDates.size(); i++) {
				itemmonth.add(GregorianCalendar.DATE, 1);
				items.add(startDates.get(i).toString());
			}
	
			adapter.setItems(items);
			adapter.notifyDataSetChanged();
		}
	};
	
	public static String getDate(long milliSeconds) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(milliSeconds);
		return formatter.format(calendar.getTime());
	}
	
	public String getCurrentDate() {
		return CalendarAdapter.dayString.get(currentPosition);
	}	
	
	public void updateDates(String dt, String tp, String ds) {
		startDates.add(dt);
		nameOfEvent.add(tp);
		descriptions.add(ds);
	}
}