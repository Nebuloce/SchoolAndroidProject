package com.example.nebul.cnaapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;



public class MainView extends Activity {
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_view);



        ImageButton calendarButton = (ImageButton)findViewById(R.id.toCalendar);
        ImageButton scheduleButton = (ImageButton)findViewById(R.id.toSchedule);
        ImageButton newsButton = (ImageButton)findViewById(R.id.toNews);
        ImageButton contactsButton = (ImageButton)findViewById(R.id.toContacts);
        ImageButton metrobusButton = (ImageButton)findViewById(R.id.toMetrobus);
        ImageButton programsButton = (ImageButton)findViewById(R.id.toPrograms);
        ImageButton infoButton = (ImageButton)findViewById(R.id.toInfo);
        ImageButton test = (ImageButton)findViewById(R.id.toInfo);

        programsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nextScreen = new Intent(getApplicationContext(), ProgramsList.class);
                startActivity(nextScreen);
            }
        });
        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent calendarScreen = new Intent(getApplicationContext(), CalendarMain.class);
                startActivity(calendarScreen);
            }
        });
        scheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newsSelect = new Intent(getApplicationContext(), ScheduleActivity.class);
                startActivity(newsSelect);
            }
        });
        newsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newsSelect = new Intent(getApplicationContext(), NewsSelect.class);
                startActivity(newsSelect);
            }
        });
        contactsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent contactsSelect = new Intent(getApplicationContext(), Contacts.class);
                startActivity(contactsSelect);
            }
        });
        metrobusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadWebsite("https://www.metrobusmobile.com/");
            }
        });
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "App Developed in part \nBy Kyrrian Munn 2017", Toast.LENGTH_SHORT).show();
            }
        });

    }
}