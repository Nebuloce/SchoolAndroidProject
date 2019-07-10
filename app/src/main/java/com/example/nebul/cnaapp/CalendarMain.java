package com.example.nebul.cnaapp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

@SuppressLint("NewApi")
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class CalendarMain extends Activity {
	public static ArrayList<String> nameOfEvent = new ArrayList<String>();
	public static ArrayList<String> startDates = new ArrayList<String>();
	public static ArrayList<String> descriptions = new ArrayList<String>();
	
	String filename = new String("cal_data.xml");	
	CCalendar c;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calendar);
		this.setTitle("Calendar");
		
		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#055b8c")));
		
		// writeFileToInternalStorage(filename, ""); // clear data file
		
		nameOfEvent.clear();
		startDates.clear();
		descriptions.clear();
		
		if(!isNetworkAvailable())
    			new AlertDialog.Builder(this).
    			setTitle("Error").setMessage("No Network Connection").
    			setNeutralButton("Close", null).show();
		else {
			new GetXML().execute("");			
		}	
	}
	
	public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) 
          getSystemService(Context.CONNECTIVITY_SERVICE);
        
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
 
        if (networkInfo != null && networkInfo.isConnected()) 
        		return true;
        else
        		return false;

    }
	
    private class GetXML extends AsyncTask<String, Void, String> {
		String src = null;
		
		@Override
		protected String doInBackground(String... params) {
    			try {
    				URL url = new URL("http://branko-cirovic.appspot.com/etcapp/events.xml");
    				HttpURLConnection con = (HttpURLConnection) url.openConnection();
    				src = readStream(con.getInputStream()); 
    			} catch (Exception e) {
    				e.printStackTrace();
    			}
    			return src;            
		}      
    
		@Override
		protected void onPostExecute(String result) { 
    			parseXML(src); // online data
    			   			
    			String old = readFileFromInternalStorage(filename); // local data
    			StringBuffer b = new StringBuffer("<events>");
    			b.append(old); b.append("</events>");
    			parseXML(b.toString());
    			   			
    			c = new CCalendar(CalendarMain.this, nameOfEvent, startDates, descriptions);
    			GridView gv = c.gridview;
			View view = gv.getRootView();
			int position = c.currentPosition;				
			gv.performItemClick(view, position, position);
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected void onProgressUpdate(Void... values) {
		}
    }  

    private String readStream(InputStream in) {
		BufferedReader reader = null;
		String line = null;
		StringBuffer sb = new StringBuffer();
		try {
			reader = new BufferedReader(new InputStreamReader(in));
			while ((line = reader.readLine()) != null) {
				sb.append(line);
	    		}    	   
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}
    
    public static void parseXML(String src) {	
		try {
			StringReader sr = new StringReader(src);
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();
			xpp.setInput(sr);
	                                
			int eventType = xpp.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if(eventType == XmlPullParser.START_TAG) {
					if(xpp.getName().equals("type")) {
						eventType = xpp.next();
						if(eventType == XmlPullParser.TEXT) {  
							nameOfEvent.add(xpp.getText());
						}
					}
				}
    	
				if(eventType == XmlPullParser.START_TAG) {
					if(xpp.getName().equals("date")) {
						eventType = xpp.next();
						if(eventType == XmlPullParser.TEXT) {
							startDates.add(xpp.getText());
						}
					}
				}
    	
				if(eventType == XmlPullParser.START_TAG) {
					if(xpp.getName().equals("description")) {
						eventType = xpp.next();
						if(eventType == XmlPullParser.TEXT) {
							descriptions.add(xpp.getText());
						}
					}
				}
				eventType = xpp.next();                	
			}    
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
	
		return true;
	}
    
    public boolean onOptionsItemSelected(MenuItem item) {
    		final String date = c.getCurrentDate();
    		final GridView gv = c.gridview;
    		c.adapter.curentDateString = date; // required for adapter; changes to current date otherwise
    		
		int id = item.getItemId();
		if(id == R.id.add) {
			LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
			View layout = inflater.inflate(R.layout.dialog_layout, (ViewGroup) findViewById(R.id.layout_root));
			
			final EditText titleBox = (EditText) layout.findViewById(R.id.title);
			final EditText descriptionBox = (EditText) layout.findViewById(R.id.description);
									
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setView(layout);
			builder.setTitle("Date: " + date);											
			
			builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {				
				@Override
				public void onClick(DialogInterface dialog, int which) {															
					String title = titleBox.getText().toString();
					String description = descriptionBox.getText().toString();						
					String src = "<event><type>" + title + "</type>" + "<date>" + date + "</date>" + "<description>" + description + "</description></event>";
					String old = readFileFromInternalStorage(filename);
					StringBuffer b = new StringBuffer(old);
					b.append(src);
					writeFileToInternalStorage(filename, b.toString()); // title and description must be non null
						
					c.updateDates(date, title, description);
					c.handler.post(c.calendarUpdater);
					
					// refresh list by "clicking" selected cell									
					View view = gv.getRootView();
					int position = c.currentPosition;										
					gv.performItemClick(view, position, position);						
															
					dialog.dismiss();					
				}
			});
			
			builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {					
					dialog.dismiss();
				}
			});
			
			final AlertDialog dialog = builder.create();			
			
			titleBox.addTextChangedListener(new TextWatcher() {
			    private void handleText() {
			        final Button okButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
			        if(titleBox.getText().length() == 0 || descriptionBox.getText().length() == 0) {
			            okButton.setEnabled(false);
			        } else {
			            okButton.setEnabled(true);
			        }
			    }
			    
			    @Override
			    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			        // Nothing to do
			    }
			    @Override
			    public void onTextChanged(CharSequence s, int start, int before, int count) {
			       // Nothing to do
			    }
				@Override
				public void afterTextChanged(Editable s) {
					handleText();					
				}
			});
			
			descriptionBox.addTextChangedListener(new TextWatcher() {
			    private void handleText() {
			        final Button okButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
			        if(titleBox.getText().length() == 0 || descriptionBox.getText().length() == 0) {
			            okButton.setEnabled(false);
			        } else {
			            okButton.setEnabled(true);
			        }
			    }
			    
			    @Override
			    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			        // Nothing to do
			    }
			    @Override
			    public void onTextChanged(CharSequence s, int start, int before, int count) {
			       // Nothing to do
			    }
				@Override
				public void afterTextChanged(Editable s) {
					handleText();					
				}
			});
						
			dialog.show();
			dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
			
		}												
	    return true;
	}
    
    public void writeFileToInternalStorage(String fileName, String data) {
    		String eol = System.getProperty("line.separator");
    		BufferedWriter writer = null;
    		try {
    			writer = new BufferedWriter(new OutputStreamWriter(openFileOutput(fileName, Context.MODE_PRIVATE)));
    			writer.write(data);
    		} catch (Exception e) {
    			e.printStackTrace();
    		} finally {
    			if (writer != null) {
    				try {
    					writer.close();
    				} catch (IOException e) {
    					e.printStackTrace();
    				}
    			}
    		}
    	}
    
    public String readFileFromInternalStorage(String fileName) {
    		String eol = System.getProperty("line.separator");
    		StringBuffer buffer = new StringBuffer();
    		BufferedReader input = null;
    		try {
    			input = new BufferedReader(new InputStreamReader(openFileInput(fileName)));
    			String line;    			
    			while ((line = input.readLine()) != null) {
    				buffer.append(line + eol);
    			}
    		} catch (Exception e) {
    			e.printStackTrace();
    		} finally {
    			if (input != null) {
    				try {
    					input.close();
    				} catch (IOException e) {
    					e.printStackTrace();
    				}
    			}
    		}
    		return buffer.toString();
    	} 
}