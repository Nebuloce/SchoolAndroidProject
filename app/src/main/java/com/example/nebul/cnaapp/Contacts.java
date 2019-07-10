package com.example.nebul.cnaapp;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.widget.ListView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Contacts extends Activity {
    String[] items = new String[] { "Call", "Write", "Visit", "Find" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_activity);
        
		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#055b8c")));        

        ListView contactListView = (ListView) findViewById(R.id.contactListView);
        contactListView.setAdapter(new ContactAdapter(this, items));
        contactListView.setTextFilterEnabled(true); 
        
        contactListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
            			int position, long id) {
                String selectedValue = (String) parent.getItemAtPosition(position);
                       
                if(selectedValue.equals("Call")) {
                    call("tel:7097587091");
                    // does not return to app after hangup
                }                                 
                else if(selectedValue.equals("Write")) {
                		sendEmail();
                }
                else if(selectedValue.equals("Visit")) {
                		loadWebsite("http://www.cna.nl.ca");
                }
                else if(selectedValue.equals("Find")) {
                		// Google maps
                }                
            }            
        });           
    }
    
    private void call(String pn) {
        PhoneCallListener phoneListener = new PhoneCallListener();
        TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneListener,
                PhoneStateListener.LISTEN_CALL_STATE);
    	
        Intent callIntent = new Intent(Intent.ACTION_DIAL); 
        callIntent.setData(Uri.parse(pn));
        try {
        		startActivity(Intent.createChooser(callIntent, "Complete Action Using"));
        } catch (android.content.ActivityNotFoundException ex) {
        		Toast.makeText(Contacts.this, "There are no phone clients installed.", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void sendEmail() {
    		if(!isNetworkAvailable())
    			new AlertDialog.Builder(Contacts.this).
    			setTitle("Error").setMessage("No Network Connection").
    			setNeutralButton("Close", null).show();
		else {
			Intent emailIntent = new Intent(Intent.ACTION_SEND);
    			emailIntent.setType("message/rfc822");
    			emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"bc170264@gmail.com"});
    			emailIntent.putExtra(Intent.EXTRA_SUBJECT, "ETC Programs Info");
    			// i.putExtra(Intent.EXTRA_TEXT, "body of email");
    			try {
    				startActivity(Intent.createChooser(emailIntent, "Complete Action Using"));
    			} catch (android.content.ActivityNotFoundException ex) {
    				Toast.makeText(Contacts.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
    			}    	
		}                               
    }
    
    public void loadWebsite(String s) {
    		Uri uri = Uri.parse(s);
    		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
    		startActivity(intent);    	    
    }
    
	public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) 
          getSystemService(Context.CONNECTIVITY_SERVICE);
        
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null
        // otherwise check if we are connected
                        
        if (networkInfo != null && networkInfo.isConnected()) 
        		return true;
        else
        		return false;

    }
    
    private class PhoneCallListener extends PhoneStateListener {

        private boolean isPhoneCalling = false;

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            if (TelephonyManager.CALL_STATE_RINGING == state) {
                // phone ringing
            }

            if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
                // active                
                isPhoneCalling = true;
            }

            if (TelephonyManager.CALL_STATE_IDLE == state) {
                // run when class initial and phone call ended, need detect flag
                // from CALL_STATE_OFFHOOK
                
            		if (isPhoneCalling) {
            			Intent i = getBaseContext().getPackageManager()
    						.getLaunchIntentForPackage(
    							getBaseContext().getPackageName());
    					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    					startActivity(i);
    					isPhoneCalling = false;
    					finish();
                }
            }
        }
    }
}